import sys


def read_score(path):
    with open(path, "r") as f:
        return float(f.read().strip())


def main():
    if len(sys.argv) != 3:
        print("Usage: python compare.py <baseline_score> <candidate_score>")
        sys.exit(1)

    baseline = read_score(sys.argv[1])
    candidate = read_score(sys.argv[2])

    drop = baseline - candidate

    # 5%p보다 많이 떨어지면 실패
    threshold = 0.05

    print("=" * 50)
    print("Prompt Regression Test")
    print("=" * 50)

    print(f"Baseline Score : {baseline:.2%}")
    print(f"Candidate Score: {candidate:.2%}")
    print(f"Score Drop     : {drop:.2%}")
    print(f"Allowed Drop   : {threshold:.2%}")

    print("=" * 50)

    if drop > threshold:
        print("❌ Regression detected")
        print("새 프롬프트의 성능이 허용 범위보다 많이 떨어졌다.")
        sys.exit(1)

    print("✅ Prompt evaluation passed")
    sys.exit(0)


if __name__ == "__main__":
    main()
