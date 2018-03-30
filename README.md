1. Окрыть проект в среде разработки (IDEA).
2. Войти в директорию ejb EmailSenderEJB создать папку lib и загрузить в эту папку ejb-interfaces-jar-4.0.0.7.1-SNAPSHOT.jar.  Этот джарник можно найти в облаке МЕГАРа, там лежит интерфейс SystemSettings
3. В теримнале в директории EmailSenderEJB
mvn install:install-file -DgroupId=ru.megar.unifo -DartifactId=ejb-interfaces-jar -Dpackaging=jar -Dversion=4.0.0.7.1-SNAPSHOT -Dfile=lib/ejb-interfaces-jar-4.0.0.7.1-SNAPSHOT.jarejb-interfaces-jar-4.0.0.7.1-SNAPSHOT.jar -DgeneratePom=true
!!! Это джарник устанавливается раз и навсегда. Поэтому если он есть у вас или при последующих сборках проекта 2 и 3 пункт делать уже не нужно, то есть пропускаем 2 и 3 пункт.
4. В теримнале переходим в директорию EmailSenderProject и запускаем команду mvn package.
5. Готовый ear файл можно найти в таргете модуля EmailSenderEAR и собственно его и деплоим в Weblogic.