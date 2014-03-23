#!/bin/bash
SRC="$1"
LIC="$HOME/git/grview/license.txt"

if [ ! -f "${LIC}" ]; then
  echo "No lic text found"
  exit 1
fi

mv "$SRC" "${SRC}.org"
cat "$LIC" "${SRC}.org" >"${SRC}"

