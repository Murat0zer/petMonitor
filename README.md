# petMonitor
Projeyi çalıştırmak için istediğini bir ide de maven projesi olaram import edin.
lombok plugin kurun.
Tomcat indirip kurun.
ide nize uygulama sunucusu olarak tomcat ekleyin.
Mysql server kurup ardından root kullanıcısı ve parolası oluşturun.
daha sonra veritabanına sunucusuna kullanıcı adı parola ile bağlanıp  bir adet schmea oluşturun. bu kullanılacak veritabanı olacak.
uygulamadaki persistence.xml dosyasını açıp içerisini aşağıdaki gibi doldurun. Burada schema daha önce olusturduğumuz schema ismiyle aynı olmalı. parola ise mysql root parolamız olmalı.

```xml
  <property name="hibernate.connection.url" value="jdbc:mysql://localhost:3306/schema" />
	<property name="hibernate.connection.username" value="root" />
	<property name="hibernate.connection.password" value="password" />
```

Daha sonra uygulama ide üzerinden çalıştırılabilir.
