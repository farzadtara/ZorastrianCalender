# Legacy Calendar Data Audit

**Input:** zoroastrian_events.json  
**Audited:** 2026-10-11  
**Method:** parsed and inspected all records; structural checks and name/flag comparisons.  
**Scope:** findings from the supplied data, not certification of religious-calendar correctness.

The JSON remains unchanged in data/legacy. The machine-readable companion is [audit_summary.json](../data/legacy/audit_summary.json).

## 1. Verified structural facts

| Measure | Result |
| --- | --- |
| File size | 61,240 bytes |
| SHA-256 | e8884078914c47c879b7bc34aa1bae59ca0a171a46ce7634929a59d9507fe2af |
| Root | JSON array |
| Records | 365 |
| Unique month/day pairs | 365 |
| Duplicate month/day pairs | 0 |
| Fields on every record | day, month, dayName, isNabor, isZoroastrianHolyday, zoroastrianHolydayTitle |
| Types | Integers for day/month, strings for names/title, booleans for flags |
| Unique dayName strings | 35 |
| Ordinary names | First 30 names occur 12 times each in the first 360 rows |
| Special-name rows | Five final rows, at 12/25–12/29 |
| True isNabor flags | 48: 47 ordinary-day rows and 1 special-day row |
| Rows with nonempty title | 76 |
| Distinct nonempty title strings | 29; this is not a count of distinct event definitions |
| True isZoroastrianHolyday flags | 7 |
| Nonempty title with holiday=false | 69 |
| Empty title with holiday=true | 0 |
| Combined title strings using “ - ” | 2 rows |
| Calendar/profile/year/era/source/version metadata | Absent |

## 2. Month layout

All month/day values are contiguous within their listed month, and records are in month/day order.

| Month | Rows | Day range | First day name |
| --- | --- | --- | --- |
| 1 | 31 | 1–31 | اورمزد |
| 2 | 31 | 1–31 | وهمن |
| 3 | 31 | 1–31 | اردیبهشت |
| 4 | 31 | 1–31 | شهریور |
| 5 | 31 | 1–31 | سپندارمزد |
| 6 | 31 | 1–31 | خرداد |
| 7 | 30 | 1–30 | امرداد |
| 8 | 30 | 1–30 | امرداد |
| 9 | 30 | 1–30 | امرداد |
| 10 | 30 | 1–30 | امرداد |
| 11 | 30 | 1–30 | امرداد |
| 12 | 29 | 1–29 | امرداد |

**Inference:** the coordinates match a non-leap Solar Hijri year, with religious day names mapped onto civil dates. They are not the twelve 30-day religious months themselves. Because the file has no calendar or year field, confirm this interpretation with the original app/reviewer before importing it into a new engine.

The first 360 day names exactly repeat the first 30 names in source order. The final five names depart from that ordinary cycle and identify special days.

## 3. Nabur findings

The ordinary source names corresponding to the proposed recurring rule are وهمن, ماه, گوش, and رام. A [community report describing Nabur](https://en.amordadnews.com/155092) names Vahman/Bahman, Mah, Gosh, and Ram. This supports using these identities as review candidates; it does not approve every date in this file.

| Day name | Total rows | Nabur=true | Nabur=false |
| --- | --- | --- | --- |
| وهمن | 12 | 12 | 0 |
| ماه | 12 | 12 | 0 |
| گوش | 12 | 11 | 1 |
| رام | 12 | 12 | 0 |
| اشتود(ماه گاتابیو) | 1 | 1 | 0 |

Two important rows:

| Source coordinate | Source value | Finding | Required response |
| --- | --- | --- | --- |
| 02/13 | dayName=گوش; isNabor=false | Inconsistent with the other 11 Gosh rows and the candidate ordinary-day rule | Review whether an exception was intentional; otherwise approve a documented correction |
| 12/26 | dayName=اشتود(ماه گاتابیو); isNabor=true | A special-day flag cannot be explained by the ordinary Roj-only rule | Review special-day practice and old calculation; do not automatically carry or delete it |

The total of 48 true flags hides these row-level differences. A count-only validation would miss them.

Map Nabur through stable religious-day IDs. Resolve English/Persian aliases such as Mah/Mohor through the reviewer; do not interpret transliteration alone as proof that the day should be مهر.

## 4. Gatha and leap-year coverage

| Source coordinate | dayName | isNabor |
| --- | --- | --- |
| 12/25 | اهنود(ماه گاتابیو) | false |
| 12/26 | اشتود(ماه گاتابیو) | true |
| 12/27 | سپندتمد(ماه گاتابیو) | false |
| 12/28 | وهوخشتر(ماه گاتابیو) | false |
| 12/29 | وهیشتوش(ماه گاتابیو) | false |

There is no 12/30 record and no year-specific leap/intercalary rule. That is not corruption in a non-leap baseline; it is incomplete coverage for a converter supporting multiple years.

Do not generate a leap-year mapping by appending a row, repeating the last name, or shifting these names without an approved profile rule. Define the five special-day identities and any additional intercalary identity explicitly.

The [calendar reference](https://www.heritageinstitute.com/zoroastrianism/calendar/index.htm) discusses Gatha/intercalary days and differing leap practices. Use it to identify review questions, not to silently declare the legacy mapping equivalent to every Fasli/Bastani convention.

## 5. Holiday flag and event semantics

The seven true holiday flags occur at 01/02, 01/03, 01/06, 01/19, 07/10, 10/05, and 11/10.

The file does not explain whether isZoroastrianHolyday means a religious observance, a community holiday, a day off, or an old UI highlight. For example, نوروز is titled on 01/01–01/04, but only 01/02 and 01/03 have a true flag.

**Do not treat all 69 titled-but-false rows as incorrect.** Preserve the legacy flag and establish separate fields for event presence, event category, and reviewed holiday classification. Never hide an observance merely because its flag is false.

## 6. Overlapping and multi-day events

| Source coordinate | Combined title |
| --- | --- |
| 03/24 | زیارت پیرسبز - زیارت ستی پیر |
| 04/10 | گاهان بار چهره میدیوشهم گاه - جشن تیرگان |

Curate these into separate event definitions/occurrences while preserving the original source title.

Repeated titles across consecutive dates suggest ranges, but not every recurrence rule can be inferred safely. One Gahambar title changes on 04/10 because a festival is concatenated to it; exact-string grouping would incorrectly split the range.

Use stable event IDs and explicit recurrence/range rules. Do not use punctuation splitting or title equality as the production calendar model.

## 7. Names, translations, and provenance

- The JSON contains names but no reviewed meanings, descriptions, English translations, or pronunciation.
- Strings such as جشن خوردادگان and the separate جشن دیگاه/جشن دیگان titles require terminology review. Do not silently fix spelling or merge events.
- The file has no rule profile, reference year, source list, approval history, schema version, or content version.
- Source categories and regional applicability are absent.
- It contains no solar/Gah calculation inputs or algorithms.

Preserve original spelling in provenance. Search normalization may handle Arabic/Persian character variants; display corrections need a recorded editorial decision.

## 8. Remediation before production

1. Confirm the legacy coordinate system, intended profile, and year applicability.
2. Obtain reviewed reference calendars and the original calculation code if available.
3. Approve year/epoch, Nowruz, day-boundary, leap/intercalary, and Nabur rules.
4. Review the two highlighted Nabur rows and holiday-flag semantics.
5. Assign stable Roj/Mah/special-day and event IDs.
6. Curate event overlap, multi-day ranges, recurrence calendars, titles, and categories.
7. Add source, language, version, and reviewer metadata.
8. Produce fixtures that prove known dates independently of the imported file.
9. Generate a corrected versioned pack and a source-preserving correction log.

**Release gate:** do not ship the raw file as a universal date engine. No religious correction was applied during this planning revision.

## 9. Reproducing structural checks

Run from the package root:

~~~python
import collections
import hashlib
import json
from pathlib import Path

p = Path("data/legacy/zoroastrian_events.json")
rows = json.loads(p.read_text(encoding="utf-8"))
pairs = collections.Counter((r["month"], r["day"]) for r in rows)
ordinary = [r["dayName"] for r in rows[:30]]

print("bytes", p.stat().st_size)
print("sha256", hashlib.sha256(p.read_bytes()).hexdigest())
print("rows", len(rows), "unique pairs", len(pairs))
print("duplicates", [pair for pair, n in pairs.items() if n > 1])
print("month counts", dict(collections.Counter(r["month"] for r in rows)))
print("ordinary cycle", all(
    r["dayName"] == ordinary[i % 30] for i, r in enumerate(rows[:360])
))
print("nabor flags", collections.Counter(
    r["dayName"] for r in rows if r["isNabor"]
))
print("title/holiday combinations", collections.Counter(
    (bool(r["zoroastrianHolydayTitle"].strip()), r["isZoroastrianHolyday"])
    for r in rows
))
~~~

These checks reproduce structural evidence. They cannot certify an epoch, leap convention, event meaning, or religious practice.
