echo '#!/bin/bash
echo "Setting up Banking System..."
cd BankingSystem
javac model/*.java service/*.java ui/*.java
echo "Compilation complete! Starting application..."
java bankingsystem.ui.BankingSystem' > run.sh
