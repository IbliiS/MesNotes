# MesNotes
echo "# Test" >> README.md
git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/IbliiS/Test.git
git push -u origin master


In order to do that:

click on the drop down menu on the toolbar at the top (usually with android icon and name of your application)
select Edit configurations
click plus sign at top left corner or press alt+insert
select Gradle
choose your module as Gradle project
in Tasks: enter assemble
press OK
press play
After that you should find your apk in directory ProjectName\build\outputs\apk
