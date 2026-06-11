from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / 'app/src/main/java/com/gajae/androidagent/core'


def assert_true(value, message):
    if not value:
        raise AssertionError(message)


def test_modules_stay_small():
    for path in SRC.glob('*.java'):
        lines = [line for line in path.read_text().splitlines() if line.strip()]
        assert_true(len(lines) <= 120, f'{path.name} is too large for weak-LLM module rule')


def test_gmail_parser_has_unread_signals():
    code = (SRC / 'GmailInboxParser.java').read_text()
    assert_true('unread' in code.lower(), 'missing unread signal')
    assert_true('읽지 않음' in code, 'missing Korean unread signal')
    assert_true('stripUnreadMarker' in code, 'missing marker stripper')


def test_fallback_decider_is_explicit():
    code = (SRC / 'FallbackDecider.java').read_text()
    assert_true('textNodeCount() < 4' in code, 'missing node threshold')
    assert_true('textLength < 24' in code, 'missing text threshold')


def test_fixture_expected_unread_count():
    fixture = (ROOT / 'tests/sample_gmail_snapshot.txt').read_text().splitlines()
    unread = [line for line in fixture if re.search(r'\|Unread,', line)]
    assert_true(len(unread) == 2, f'expected 2 unread rows, got {len(unread)}')


def test_prd_contains_granular_plan():
    doc = (ROOT / 'docs/PLAN_PRD_SCENARIOS.md').read_text()
    for term in ['Module size rules', 'below 120 lines', 'GmailScenario.java']:
        assert_true(term in doc, f'missing {term}')


def test_universal_agent_modules_exist():
    for name in ['UniversalAppAgent.java', 'NaturalInstructionParser.java', 'GenericTaskPlanner.java']:
        assert_true((SRC / name).exists(), f'missing {name}')


def test_universal_docs_are_honest():
    doc = (ROOT / 'docs/UNIVERSAL_AGENT.md').read_text()
    assert_true('generalized beyond Gmail' in doc, 'missing generalization claim')
    assert_true('does not yet complete arbitrary multi-step tasks' in doc or 'observe-plan-act' in doc, 'missing limitation')
    assert_true('Real VLM' in doc, 'missing VLM limitation')


def test_product_hardening_modules_exist():
    for name in ['ClosedLoopExecutor.java', 'HttpVlmClient.java', 'ApiConfig.java', 'TargetMatcher.java']:
        assert_true((SRC / name).exists(), f'missing {name}')
    assert_true((ROOT / 'app/src/main/java/com/gajae/androidagent/ConfigStore.java').exists(), 'missing ConfigStore')


def test_readme_points_to_signed_apk():
    doc = (ROOT / 'README.md').read_text()
    assert_true('out/gajae-android-agent.apk' in doc, 'missing signed APK path')
    assert_true('API endpoint' in doc, 'missing API setup instructions')


if __name__ == '__main__':
    tests = [value for name, value in sorted(globals().items()) if name.startswith('test_')]
    for test in tests:
        test()
        print(f'PASS {test.__name__}')
    print(f'{len(tests)} tests passed')
