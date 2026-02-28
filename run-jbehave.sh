#!/usr/bin/env zsh
set -euo pipefail

# Run all JBehave test suites using the all-jbehave profile
# Usage: ./run-jbehave.sh

ROOT_DIR="$(dirname "$0")"
cd "$ROOT_DIR"

MAVEN="./mvnw"
if [ ! -x "$MAVEN" ]; then
  MAVEN="mvn"
fi

echo "================================================================"
echo "Running all JBehave tests"
echo "================================================================"

if $MAVEN -Pall-jbehave test; then
  echo
  echo "All JBehave tests passed!"
  echo
  echo "Report locations:"
  echo "  - target/jbehave"
  echo "  - target/jbehave/view"
  echo "  - target/surefire-reports"
  exit 0
else
  echo
  echo "JBehave tests failed!"
  echo "Check the reports for details:"
  echo "  - target/surefire-reports"
  echo "  - target/jbehave"
  exit 1
fi
