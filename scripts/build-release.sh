#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
bash gradlew :app:assembleRelease -PdisableR8=true -Pandroid.newDsl=false -Pandroid.builtInKotlin=false -PreleaseVersionName=0.1.0-rc.1 -PreleaseVersionCode=248 "$@"
python3 - <<'PYBUILD'
import json, pathlib, shutil, hashlib
folder = pathlib.Path('app/build/outputs/apk/release')
metadata = json.loads((folder/'output-metadata.json').read_text())
apk = folder/metadata['elements'][0]['outputFile']
out = pathlib.Path('dist'); out.mkdir(exist_ok=True)
target = out/'c001apk-reply-fix-0.1.0-rc.1.apk'
shutil.copy2(apk, target)
(out/'SHA256SUMS').write_text(hashlib.sha256(target.read_bytes()).hexdigest()+'  '+target.name+'\n')
print('Built '+str(target))
PYBUILD
