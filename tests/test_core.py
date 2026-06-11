from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / 'app/src/main/java/com/gajae/androidagent/core'


def read(name):
    return (SRC / name).read_text()


def test_modules_stay_small():
    for path in SRC.glob('*.java'):
        lines = [line for line in path.read_text().splitlines() if line.strip()]
        assert len(lines) <= 120, f'{path.name} is too large for weak-LLM module rule'


def test_gmail_parser_has_unread_signals():
    code = read('GmailInboxParser.java')
    assert 'unread' in code.lower()
    assert '읽지 않음' in code
    assert 'stripUnreadMarker' in code


def test_fallback_decider_is_explicit():
    code = read('FallbackDecider.java')
    assert 'textNodeCount() < 4' in code
    assert 'textLength < 24' in code


def test_fixture_expected_unread_count():
    fixture = (ROOT / 'tests/sample_gmail_snapshot.txt').read_text().splitlines()
    unread = [line for line in fixture if re.search(r'\|Unread,', line)]
    assert len(unread) == 2


def test_prd_contains_granular_plan():
    doc = (ROOT / 'docs/PLAN_PRD_SCENARIOS.md').read_text()
    assert 'Module size rules' in doc
    assert 'below 120 lines' in doc
    assert 'GmailScenario.java' in doc
