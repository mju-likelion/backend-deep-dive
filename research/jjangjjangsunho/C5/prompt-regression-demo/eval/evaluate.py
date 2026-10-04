import json
import os
from pathlib import Path

from openai import OpenAI


BASE_DIR = Path(__file__).resolve().parent.parent

PROMPT_PATH = BASE_DIR / "prompts" / "system.txt"
GOLDEN_SET_PATH = BASE_DIR / "eval" / "golden_set.json"


def load_prompt():
    return PROMPT_PATH.read_text(encoding="utf-8").strip()


def load_golden_set():
    with open(GOLDEN_SET_PATH, "r", encoding="utf-8") as f:
        return json.load(f)


def main():
    client = OpenAI(api_key=os.environ["OPENAI_API_KEY"])

    system_prompt = load_prompt()
    golden_set = load_golden_set()

    passed = 0
    total = len(golden_set)

    for item in golden_set:
        response = client.responses.create(
            model="gpt-5.6",
            instructions=system_prompt,
            input=item["input"],
        )

        answer = response.output_text.strip()
        expected = item["expected"]

        is_correct = expected.lower() in answer.lower()

        if is_correct:
            passed += 1
            result = "PASS"
        else:
            result = "FAIL"

        print(f'Q{item["id"]} {result}')
        print(f'Question : {item["input"]}')
        print(f'Expected : {expected}')
        print(f'Answer   : {answer}')
        print("-" * 50)

    score = passed / total

    print(f"Score: {passed}/{total}")
    print(f"Accuracy: {score:.2%}")

    with open(BASE_DIR / "score.txt", "w") as f:
        f.write(str(score))


if __name__ == "__main__":
    main()
