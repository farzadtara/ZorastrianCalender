"use strict";

const LEGACY_DATA_URL = "../data/legacy/zoroastrian_events.json";
const CALENDARS = {
  gregorian: { locale: "en-US-u-ca-gregory-nu-latn", offset: 0 },
  persian: { locale: "en-US-u-ca-persian-nu-latn", offset: 621 },
  islamic: { locale: "en-US-u-ca-islamic-umalqura-nu-latn", offset: 579 },
};
const WEEKDAYS_SATURDAY = ["شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه"];
const WEEKDAYS_SUNDAY = ["یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه", "شنبه"];
const formatters = Object.fromEntries(Object.entries(CALENDARS).map(([name, config]) => [
  name,
  new Intl.DateTimeFormat(config.locale, {
    timeZone: "UTC", year: "numeric", month: "numeric", day: "numeric",
  }),
]));
const state = {
  legacy: [],
  bySolarDate: new Map(),
  screen: "calendar",
  selected: dateFromIso(todayTehranIso()),
  source: "gregorian",
  converterIso: todayTehranIso(),
  eventFilter: "all",
  eventQuery: "",
  eventsLimit: 12,
  settings: loadSettings(),
  qibla: { bearing: null, heading: null, locationReady: false },
};

function loadSettings() {
  const defaults = { theme: "dark", digits: "fa", weekStart: "sat", naburReminder: false, eventReminder: false };
  try { return { ...defaults, ...JSON.parse(localStorage.getItem("zoro-prototype-settings") || "{}") }; }
  catch { return defaults; }
}

function saveSettings() {
  try { localStorage.setItem("zoro-prototype-settings", JSON.stringify(state.settings)); }
  catch { /* Preferences are optional when browser storage is unavailable. */ }
}

function todayTehranIso() {
  const parts = new Intl.DateTimeFormat("en-CA", {
    timeZone: "Asia/Tehran", year: "numeric", month: "2-digit", day: "2-digit",
  }).formatToParts(new Date());
  const values = Object.fromEntries(parts.filter((part) => part.type !== "literal").map((part) => [part.type, part.value]));
  return `${values.year}-${values.month}-${values.day}`;
}

function dateFromIso(value) {
  if (!/^\d{4}-\d{2}-\d{2}$/.test(value || "")) return null;
  const [year, month, day] = value.split("-").map(Number);
  const date = new Date(0);
  date.setUTCHours(12, 0, 0, 0);
  date.setUTCFullYear(year, month - 1, day);
  if (date.getUTCFullYear() !== year || date.getUTCMonth() !== month - 1 || date.getUTCDate() !== day) return null;
  return date;
}

function dateToIso(date) {
  return date.toISOString().slice(0, 10);
}

function shiftDate(date, amount) {
  const shifted = new Date(date);
  shifted.setUTCDate(shifted.getUTCDate() + amount);
  return shifted;
}

function calendarParts(date, calendar) {
  const values = Object.fromEntries(formatters[calendar].formatToParts(date)
    .filter((part) => part.type !== "literal")
    .map((part) => [part.type, Number.parseInt(part.value, 10)]));
  return { year: values.year, month: values.month, day: values.day };
}

function sameMonth(left, right) {
  const a = calendarParts(left, "persian");
  const b = calendarParts(right, "persian");
  return a.year === b.year && a.month === b.month;
}

function solarMonthBounds(date) {
  let first = new Date(date);
  for (let i = 0; i < 32 && sameMonth(shiftDate(first, -1), date); i += 1) first = shiftDate(first, -1);
  const parts = calendarParts(date, "persian");
  let length = 1;
  while (length < 32 && sameMonth(shiftDate(first, length), date)) length += 1;
  return { first, length, parts };
}

function persianMonthName(date) {
  return new Intl.DateTimeFormat("fa-IR-u-ca-persian", { timeZone: "UTC", month: "long" }).format(date);
}

function formatCalendarDate(date, calendar) {
  const locale = calendar === "gregorian" ? "fa-IR-u-ca-gregory" :
    calendar === "islamic" ? "fa-IR-u-ca-islamic-umalqura" : "fa-IR-u-ca-persian";
  return new Intl.DateTimeFormat(locale, {
    timeZone: "UTC", year: "numeric", month: "long", day: "numeric",
    numberingSystem: state.settings.digits === "fa" ? "arabext" : "latn",
  }).format(date);
}

function formatNumber(value) {
  return new Intl.NumberFormat(state.settings.digits === "fa" ? "fa-IR" : "en-US", { maximumFractionDigits: 0 }).format(value);
}

function escapeHtml(value) {
  return String(value).replace(/[&<>"']/g, (char) => ({
    "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;",
  })[char]);
}

function legacyRecord(date) {
  if (!date) return null;
  const { month, day } = calendarParts(date, "persian");
  return state.bySolarDate.get(`${month}/${day}`) || null;
}

async function loadLegacyData() {
  try {
    const response = await fetch(LEGACY_DATA_URL, { cache: "no-cache" });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    state.legacy = await response.json();
    state.bySolarDate = new Map(state.legacy.map((row) => [`${row.month}/${row.day}`, row]));
  } catch {
    state.legacy = [];
    state.bySolarDate.clear();
    document.querySelectorAll(".notice").forEach((notice) => {
      notice.classList.add("notice-error");
      const message = notice.querySelector("p");
      if (message && notice.closest("#screen-calendar")) message.textContent = "فایل داده قدیمی در دسترس نیست؛ فقط تاریخ‌های مدنی نمایش داده می‌شوند.";
    });
  }
}

function renderCalendar() {
  const date = state.selected;
  const parts = calendarParts(date, "persian");
  const record = legacyRecord(date);
  const bounds = solarMonthBounds(date);
  const firstWeekday = bounds.first.getUTCDay();
  const offset = state.settings.weekStart === "sat" ? (firstWeekday + 1) % 7 : firstWeekday;
  const weekdayNames = state.settings.weekStart === "sat" ? WEEKDAYS_SATURDAY : WEEKDAYS_SUNDAY;
  const weekdayRow = document.getElementById("weekday-row");
  weekdayRow.innerHTML = weekdayNames.map((day) => `<span>${day.slice(0, 1)}</span>`).join("");

  document.getElementById("hero-persian-date").textContent = formatCalendarDate(date, "persian");
  document.getElementById("hero-other-dates").textContent = `میلادی ${formatCalendarDate(date, "gregorian")}  ·  قمری ${formatCalendarDate(date, "islamic")}`;
  document.getElementById("hero-zoro-day").textContent = record?.dayName || "در داده ثبت نشده";
  document.getElementById("hero-nabur").hidden = !record?.isNabor;
  const heroEvent = document.getElementById("hero-event");
  heroEvent.hidden = !record?.zoroastrianHolydayTitle;
  heroEvent.textContent = record?.zoroastrianHolydayTitle || "";
  document.getElementById("month-title").textContent = `${persianMonthName(bounds.first)} ${formatNumber(parts.year)}`;
  document.getElementById("month-subtitle").textContent = `ماه ${formatNumber(parts.month)} · داده دینی تأییدنشده`;
  document.getElementById("selected-day-title").textContent = formatCalendarDate(date, "persian");

  const today = todayTehranIso();
  const grid = document.getElementById("calendar-grid");
  const firstCell = shiftDate(bounds.first, -offset);
  grid.innerHTML = Array.from({ length: 42 }, (_, index) => {
    const cellDate = shiftDate(firstCell, index);
    const cellIso = dateToIso(cellDate);
    const cellParts = calendarParts(cellDate, "persian");
    const cellRecord = legacyRecord(cellDate);
    const currentMonth = cellParts.year === parts.year && cellParts.month === parts.month;
    const classes = ["calendar-cell"];
    if (!currentMonth) classes.push("adjacent");
    if (cellIso === today) classes.push("today");
    if (cellIso === dateToIso(date)) classes.push("selected");
    if (cellDate.getUTCDay() === 5) classes.push("friday");
    const markers = `${cellRecord?.zoroastrianHolydayTitle ? '<i class="cell-marker" aria-hidden="true"></i>' : ""}${cellRecord?.isNabor ? '<i class="cell-marker nabur" aria-hidden="true"></i>' : ""}`;
    const markerLabels = [cellRecord?.isNabor ? "، نابور در داده قدیمی" : "", cellRecord?.zoroastrianHolydayTitle ? "، مناسبت در داده قدیمی" : ""].join("");
    const label = `${formatCalendarDate(cellDate, "persian")}${cellRecord?.dayName ? `، ${cellRecord.dayName}` : ""}${markerLabels}`;
    return `<button type="button" aria-label="${escapeHtml(label)}" aria-pressed="${cellIso === dateToIso(date)}" data-select-date="${cellIso}" class="${classes.join(" ")}"><span class="cell-number">${formatNumber(cellParts.day)}</span><span class="cell-markers">${markers}</span></button>`;
  }).join("");

  const selectedContent = document.getElementById("selected-day-content");
  const rows = [
    ["روز زرتشتی در داده قدیمی", record?.dayName || "ثبت نشده"],
    ["وضعیت نابور در داده قدیمی", record ? (record.isNabor ? "بله" : "خیر") : "ثبت نشده"],
  ];
  if (record?.zoroastrianHolydayTitle) rows.push(["عنوان مناسبت در داده قدیمی", record.zoroastrianHolydayTitle]);
  selectedContent.innerHTML = `<div class="day-detail-list">${rows.map(([labelText, value]) => `<div class="detail-line"><span>${labelText}</span><strong>${escapeHtml(value)}</strong></div>`).join("")}</div>`;
}

function renderEvents() {
  const query = normalizeText(state.eventQuery.trim());
  const rows = state.legacy.filter((row) => {
    if (!row.zoroastrianHolydayTitle) return false;
    if (state.eventFilter === "nabur" && !row.isNabor) return false;
    const haystack = normalizeText(`${row.zoroastrianHolydayTitle} ${row.dayName} ${row.month}/${row.day}`);
    return !query || haystack.includes(query);
  });
  const visible = rows.slice(0, state.eventsLimit);
  document.getElementById("event-list").innerHTML = visible.length ? visible.map((row) => `
    <article class="event-card">
      <div class="event-date"><span>${formatNumber(row.day)}</span><small>ماه ${formatNumber(row.month)}</small></div>
      <div class="event-copy"><strong>${escapeHtml(row.zoroastrianHolydayTitle)}</strong><span>${escapeHtml(row.dayName)}${row.isNabor ? " · نابور در داده قدیمی" : ""}</span></div>
    </article>`).join("") : `<div class="empty-state">${state.legacy.length ? "موردی با این جست‌وجو پیدا نشد." : "داده قدیمی بارگذاری نشده است."}</div>`;
  const more = document.getElementById("more-events");
  more.hidden = rows.length <= state.eventsLimit;
  more.textContent = `نمایش ${formatNumber(Math.min(12, rows.length - state.eventsLimit))} مورد دیگر`;
}

function renderConverter() {
  document.querySelectorAll("[data-source]").forEach((button) => {
    const active = button.dataset.source === state.source;
    button.classList.toggle("active", active);
    button.setAttribute("aria-pressed", String(active));
  });
  const selectedDate = dateFromIso(state.converterIso) || state.selected;
  const p = calendarParts(selectedDate, state.source === "islamic" ? "islamic" : state.source);
  const input = document.getElementById("converter-input");
  if (state.source === "gregorian") {
    input.innerHTML = `<label class="field-label" for="converter-date">تاریخ میلادی</label><input class="date-input" id="converter-date" type="date" value="${dateToIso(selectedDate)}" required />`;
  } else {
    const calendarLabel = state.source === "persian" ? "شمسی" : "قمری";
    input.innerHTML = `<div class="numeric-date"><label>سال ${calendarLabel}<input id="input-year" inputmode="numeric" type="text" value="${p.year}" aria-label="سال ${calendarLabel}" /></label><label>ماه<input id="input-month" inputmode="numeric" type="text" value="${p.month}" aria-label="ماه ${calendarLabel}" /></label><label>روز<input id="input-day" inputmode="numeric" type="text" value="${p.day}" aria-label="روز ${calendarLabel}" /></label></div>`;
  }
  document.getElementById("converter-error").hidden = true;
  renderConverterResults(selectedDate);
}

function renderConverterResults(date) {
  const record = legacyRecord(date);
  const legacyMarkup = record ? `<div class="result-legacy">روز در داده زرتشتی قدیمی<strong>${escapeHtml(record.dayName)}</strong>${record.zoroastrianHolydayTitle ? `<span>${escapeHtml(record.zoroastrianHolydayTitle)}</span>` : ""}</div>` : `<div class="result-legacy">برای این تاریخ در فایل قدیمی رکوردی نیست.</div>`;
  document.getElementById("converter-results").innerHTML = `
    <h3 class="result-heading">نتیجه تبدیل</h3>
    <div class="result-row"><span>میلادی</span><strong>${formatCalendarDate(date, "gregorian")}</strong></div>
    <div class="result-row"><span>هجری شمسی</span><strong>${formatCalendarDate(date, "persian")}</strong></div>
    <div class="result-row"><span>هجری قمریِ ام‌القری</span><strong>${formatCalendarDate(date, "islamic")}</strong></div>
    ${legacyMarkup}`;
}

function normalizeDigits(text) {
  return String(text).replace(/[۰-۹٠-٩]/g, (digit) => {
    const code = digit.charCodeAt(0);
    return String(code >= 0x06f0 ? code - 0x06f0 : code - 0x0660);
  });
}

function normalizeText(text) {
  return text.replace(/[ي]/g, "ی").replace(/[ك]/g, "ک").replace(/\s+/g, " ").toLocaleLowerCase();
}

function findGregorianDate(year, month, day, calendar) {
  const config = CALENDARS[calendar];
  if (!config || !Number.isInteger(year) || !Number.isInteger(month) || !Number.isInteger(day)) return null;
  if (year < (calendar === "gregorian" ? 1 : 100) || year > 9999 || month < 1 || month > 12 || day < 1 || day > 31) return null;
  const approximateYear = year + config.offset;
  const start = new Date(Date.UTC(approximateYear - 1, 0, 1, 12));
  for (let i = 0; i < 1100; i += 1) {
    const candidate = shiftDate(start, i);
    const parts = calendarParts(candidate, calendar);
    if (parts.year === year && parts.month === month && parts.day === day) return candidate;
  }
  return null;
}

function parseNumberInput(value) {
  const normalized = normalizeDigits(value).trim();
  return /^\d+$/.test(normalized) ? Number(normalized) : NaN;
}

function convertForm(event) {
  event.preventDefault();
  const error = document.getElementById("converter-error");
  let date = null;
  if (state.source === "gregorian") {
    date = dateFromIso(document.getElementById("converter-date")?.value);
  } else {
    const calendar = state.source;
    const year = parseNumberInput(document.getElementById("input-year")?.value);
    const month = parseNumberInput(document.getElementById("input-month")?.value);
    const day = parseNumberInput(document.getElementById("input-day")?.value);
    date = findGregorianDate(year, month, day, calendar);
  }
  if (!date) {
    error.textContent = "این تاریخ معتبر نیست یا در تقویم مرورگر پشتیبانی نمی‌شود.";
    error.hidden = false;
    return;
  }
  state.converterIso = dateToIso(date);
  error.hidden = true;
  renderConverterResults(date);
}

function moveSolarMonth(amount) {
  const bounds = solarMonthBounds(state.selected);
  const currentDay = calendarParts(state.selected, "persian").day;
  const probe = shiftDate(bounds.first, amount > 0 ? bounds.length : -1);
  const target = solarMonthBounds(probe);
  setSelectedDate(shiftDate(target.first, Math.min(currentDay, target.length) - 1));
  renderCalendar();
}

function setSelectedDate(date) {
  state.selected = date;
  state.converterIso = dateToIso(date);
}

function setScreen(screen) {
  state.screen = screen;
  document.querySelectorAll(".screen").forEach((element) => {
    const active = element.id === `screen-${screen}`;
    element.hidden = !active;
    element.classList.toggle("active", active);
  });
  document.querySelectorAll(".nav-item").forEach((button) => {
    const active = button.dataset.screenTarget === screen;
    button.classList.toggle("active", active);
    if (active) button.setAttribute("aria-current", "page");
    else button.removeAttribute("aria-current");
  });
  const titles = { calendar: "تقویم", events: "مناسبت‌ها", converter: "تبدیل تاریخ", qibla: "قبله‌نما", settings: "تنظیمات" };
  document.getElementById("screen-title").textContent = titles[screen];
  if (screen === "calendar") renderCalendar();
  if (screen === "events") renderEvents();
  if (screen === "converter") renderConverter();
  if (screen === "settings") renderSettings();
}

function renderSettings() {
  document.body.dataset.theme = state.settings.theme;
  const theme = document.getElementById("theme-toggle");
  theme.textContent = state.settings.theme === "dark" ? "تیره" : "روشن";
  theme.setAttribute("aria-pressed", String(state.settings.theme === "light"));
  const digits = document.getElementById("digits-toggle");
  digits.textContent = state.settings.digits === "fa" ? "فارسی" : "لاتین";
  digits.setAttribute("aria-pressed", String(state.settings.digits === "en"));
  const week = document.getElementById("week-toggle");
  week.textContent = state.settings.weekStart === "sat" ? "شنبه" : "یکشنبه";
  week.setAttribute("aria-pressed", String(state.settings.weekStart === "sun"));
  document.querySelectorAll("[data-setting]").forEach((button) => {
    button.setAttribute("aria-checked", String(Boolean(state.settings[button.dataset.setting])));
  });
}

function qiblaBearing(latitude, longitude) {
  const radians = Math.PI / 180;
  const lat1 = latitude * radians;
  const lat2 = 21.4225 * radians;
  const deltaLongitude = (39.8262 - longitude) * radians;
  const y = Math.sin(deltaLongitude);
  const x = Math.cos(lat1) * Math.tan(lat2) - Math.sin(lat1) * Math.cos(deltaLongitude);
  return (Math.atan2(y, x) / radians + 360) % 360;
}

function updateQibla() {
  if (state.qibla.bearing === null) return;
  const relative = (state.qibla.bearing - (state.qibla.heading || 0) + 360) % 360;
  document.getElementById("qibla-needle").style.transform = `translate(-50%, -50%) rotate(${relative}deg)`;
  document.getElementById("qibla-degree").textContent = `${formatNumber(Math.round(state.qibla.bearing))}°`;
  document.getElementById("qibla-description").textContent = state.qibla.heading === null
    ? "زاویه نسبت به شمال؛ چرخش قطب‌نما در این مرورگر در دسترس نیست."
    : "جهت تقریبی قبله با حسگر دستگاه؛ برای دقت بهتر قطب‌نما را کالیبره کنید.";
}

async function enableQibla() {
  const description = document.getElementById("qibla-description");
  const button = document.getElementById("enable-location");
  if (!navigator.geolocation) {
    description.textContent = "این مرورگر موقعیت مکانی را پشتیبانی نمی‌کند.";
    return;
  }
  button.disabled = true;
  button.textContent = "در حال دریافت موقعیت…";
  navigator.geolocation.getCurrentPosition(async (position) => {
    state.qibla.bearing = qiblaBearing(position.coords.latitude, position.coords.longitude);
    state.qibla.locationReady = true;
    if (typeof DeviceOrientationEvent !== "undefined") {
      try {
        if (typeof DeviceOrientationEvent.requestPermission === "function") {
          const permission = await DeviceOrientationEvent.requestPermission();
          if (permission !== "granted") throw new Error("denied");
        }
        const updateHeading = (event) => {
          const heading = Number.isFinite(event.webkitCompassHeading)
            ? event.webkitCompassHeading
            : event.absolute && Number.isFinite(event.alpha) ? (360 - event.alpha) % 360 : null;
          if (heading !== null) {
            state.qibla.heading = heading;
            updateQibla();
          }
        };
        window.addEventListener("deviceorientationabsolute", updateHeading, true);
        window.addEventListener("deviceorientation", updateHeading, true);
      } catch { state.qibla.heading = null; }
    }
    updateQibla();
    button.disabled = false;
    button.textContent = "به‌روزرسانی موقعیت";
  }, (error) => {
    const messages = {
      1: "اجازه موقعیت داده نشد؛ برای محاسبه قبله می‌توانید دوباره تلاش کنید.",
      2: "موقعیت فعلی در دسترس نیست.",
      3: "دریافت موقعیت بیش از حد طول کشید؛ دوباره تلاش کنید.",
    };
    description.textContent = messages[error.code] || "دریافت موقعیت ناموفق بود.";
    button.disabled = false;
    button.textContent = "تلاش دوباره";
  }, { enableHighAccuracy: true, timeout: 12000, maximumAge: 300000 });
}

function bindEvents() {
  document.querySelectorAll("[data-screen-target]").forEach((button) => button.addEventListener("click", () => setScreen(button.dataset.screenTarget)));
  document.getElementById("today-button").addEventListener("click", () => {
    setSelectedDate(dateFromIso(todayTehranIso()));
    renderCalendar();
    setScreen("calendar");
  });
  document.getElementById("selected-today-button").addEventListener("click", () => {
    setSelectedDate(dateFromIso(todayTehranIso()));
    renderCalendar();
  });
  document.querySelectorAll("[data-month-step]").forEach((button) => button.addEventListener("click", () => moveSolarMonth(Number(button.dataset.monthStep))));
  document.getElementById("calendar-grid").addEventListener("click", (event) => {
    const button = event.target.closest("[data-select-date]");
    if (!button) return;
    setSelectedDate(dateFromIso(button.dataset.selectDate));
    renderCalendar();
  });
  document.querySelectorAll("[data-source]").forEach((button) => button.addEventListener("click", () => {
    state.source = button.dataset.source;
    renderConverter();
  }));
  document.getElementById("converter-form").addEventListener("submit", convertForm);
  document.getElementById("event-search").addEventListener("input", (event) => {
    state.eventQuery = event.target.value;
    state.eventsLimit = 12;
    renderEvents();
    const input = document.getElementById("event-search");
    input.focus();
    input.setSelectionRange(input.value.length, input.value.length);
  });
  document.querySelectorAll("[data-event-filter]").forEach((button) => button.addEventListener("click", () => {
    state.eventFilter = button.dataset.eventFilter;
    state.eventsLimit = 12;
    document.querySelectorAll("[data-event-filter]").forEach((item) => {
      const active = item === button;
      item.classList.toggle("active", active);
      item.setAttribute("aria-pressed", String(active));
    });
    renderEvents();
  }));
  document.getElementById("more-events").addEventListener("click", () => {
    state.eventsLimit += 12;
    renderEvents();
  });
  document.getElementById("theme-toggle").addEventListener("click", () => {
    state.settings.theme = state.settings.theme === "dark" ? "light" : "dark";
    saveSettings(); renderSettings();
  });
  document.getElementById("digits-toggle").addEventListener("click", () => {
    state.settings.digits = state.settings.digits === "fa" ? "en" : "fa";
    saveSettings(); renderSettings(); renderCalendar();
    if (state.screen === "events") renderEvents();
    if (state.screen === "converter") renderConverter();
  });
  document.getElementById("week-toggle").addEventListener("click", () => {
    state.settings.weekStart = state.settings.weekStart === "sat" ? "sun" : "sat";
    saveSettings(); renderSettings(); renderCalendar();
  });
  document.querySelectorAll("[data-setting]").forEach((button) => button.addEventListener("click", () => {
    const key = button.dataset.setting;
    state.settings[key] = !state.settings[key];
    saveSettings(); renderSettings();
  }));
  document.getElementById("enable-location").addEventListener("click", enableQibla);
}

function runSelfCheck() {
  const sample = dateFromIso("2026-10-11");
  const solar = calendarParts(sample, "persian");
  const inverse = findGregorianDate(1405, 7, 19, "persian");
  const checks = [
    [solar.year === 1405 && solar.month === 7 && solar.day === 19, "Solar Hijri conversion for 2026-10-11"],
    [inverse && dateToIso(inverse) === "2026-10-11", "Solar Hijri reverse conversion"],
    [findGregorianDate(1405, 13, 1, "persian") === null, "reject invalid Solar Hijri month"],
    [dateFromIso("2026-02-31") === null, "reject invalid Gregorian day"],
    [legacyRecord(sample)?.dayName === "ارد", "legacy JSON lookup at 7/19"],
    [qiblaBearing(35.6892, 51.3890) > 215 && qiblaBearing(35.6892, 51.3890) < 221, "Qibla bearing for Tehran"],
  ];
  const failed = checks.filter(([ok]) => !ok);
  const node = document.getElementById("self-check");
  node.hidden = false;
  node.textContent = failed.length
    ? `בדיקה פנימית נכשלה: ${failed.map(([, name]) => name).join("، ")}`
    : `בדיקה פנימית עברה · ${formatNumber(checks.length)} בדיקות`;
  if (failed.length) node.setAttribute("data-failed", "true");
}

async function start() {
  document.body.dataset.theme = state.settings.theme;
  bindEvents();
  await loadLegacyData();
  renderSettings();
  renderCalendar();
  state.converterIso = dateToIso(state.selected);
  if (new URLSearchParams(location.search).has("selfcheck")) runSelfCheck();
  if ("serviceWorker" in navigator && (location.protocol === "https:" || location.hostname === "localhost")) {
    navigator.serviceWorker.register("service-worker.js").catch(() => {});
  }
}

start();
