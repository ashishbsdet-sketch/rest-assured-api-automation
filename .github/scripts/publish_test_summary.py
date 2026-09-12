from __future__ import annotations

import glob
import os
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

report_files = glob.glob("target/surefire-reports/TEST-*.xml")

totals = {
    "tests": 0,
    "failures": 0,
    "errors": 0,
    "skipped": 0,
    "time": 0.0,
}

for report_file in report_files:
    root = ET.parse(report_file).getroot()
    totals["tests"] += int(root.attrib.get("tests", 0))
    totals["failures"] += int(root.attrib.get("failures", 0))
    totals["errors"] += int(root.attrib.get("errors", 0))
    totals["skipped"] += int(root.attrib.get("skipped", 0))
    totals["time"] += float(root.attrib.get("time", 0.0))

failed = totals["failures"] + totals["errors"]
passed = totals["tests"] - failed - totals["skipped"]
icon = "✅" if report_files and failed == 0 else "❌"

if not report_files:
    headline = "❌ Test reports were not generated"
else:
    headline = (
        f"{icon} {passed} passed, {failed} failed, "
        f"{totals['skipped']} skipped"
    )

summary = f"""## API test results

### {headline}

| Total | Passed | Failed | Skipped | Duration |
| ---: | ---: | ---: | ---: | ---: |
| {totals['tests']} | {passed} | {failed} | {totals['skipped']} | {totals['time']:.2f}s |

[TestNG reports are available in the workflow artifacts.](#artifacts)
"""

print(headline)

summary_path = os.getenv("GITHUB_STEP_SUMMARY")
if summary_path:
    Path(summary_path).open("a", encoding="utf-8").write(summary)

output_path = os.getenv("GITHUB_OUTPUT")
if output_path:
    with Path(output_path).open("a", encoding="utf-8") as output:
        output.write(f"total={totals['tests']}\n")
        output.write(f"passed={passed}\n")
        output.write(f"failed={failed}\n")
        output.write(f"skipped={totals['skipped']}\n")

if not report_files:
    print("::error title=Test reports missing::Surefire did not create any TEST-*.xml files")
    sys.exit(1)

if failed:
    print(f"::error title=API tests failed::{failed} test execution(s) failed")
else:
    print(f"::notice title=API tests passed::{passed} passed, {totals['skipped']} skipped")
