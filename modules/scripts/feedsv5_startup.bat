@echo off

SETLOCAL

set scripts_dir="c:\Users\eljol\src\turfgame\modules\scripts\"
set jar_dir="c:\Users\eljol\.m2\repository\org\joelson\turf\resources\0.1-SNAPSHOT"
set feed_drive=c:
set feed_dir="c:\Users\eljol\src\turfgame_feedsv5"

echo scripts:  %scripts_dir%
echo jar dir:  %jar_dir%
echo drive:    %feed_drive%
echo feed dir: %feed_dir%

@echo on
:loop
%feed_drive%
cd %feed_dir%
%scripts_dir%feedsv5.bat %jar_dir%
goto loop

ENDLOCAL