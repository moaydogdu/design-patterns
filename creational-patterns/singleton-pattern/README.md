# Singleton Pattern (Tekil Nesne Tasarımı)

Singleton pattern, bir nesnenin tekil olmasını garanti altına almak,
birden fazla kopyasının olmamasını sağlamak için kullanılan
bir tasarım desenidir.

Bu sayede çalışma zamanında singleton olarak tasarlanan nesneye
her erişim sağlandığında onun yeni bir instancesine değil
her zaman aynı instanceye erişim sağlanır.

Constructor metodu private olarak tanımlanır ve nesne kendi içerisinde
statik tanımlama ile kendi örneğini barındırır. Nesneye her erişim sağlandığında
yani bir getInstance metodu ile örnek alınmaya çalışıldığında döndürülen aslında
ilk oluşturulan ve statik olarak saklanan nesnenin orjinal instancesidir.

### Basit Hali;

```
public class Singleton {

    private static Singleton instance;
    private String data; //Example data. It can be anything.

    private Singleton(String data){
        this.data = data;
    }

    public static Singleton getInstance(String data){

       if(instance == null){
           instance = new Singleton(data);
       }
        return instance;
    }
}
```

Yukarıdaki gösterim, bir singleton tasarıma sahip sınıfın en basit halidir.
Eğer daha önce oluşturulmamış ise bir instance oluşturur, eğer oluşturulmuş bir
instance varsa onu geriye döndürür.

**Fakat burada bir başka sorun oluşmaktadır : Thread Concurrency**

Aynı anda iki farklı thread tarafından bu metoda erişilmesi durumunda geriye 
iki farklı instance dönecektir. Bu da tasarımın maksadı gereği 
istemeyeceğimiz bir şey.

Bu durumda ***synchronized*** keywordünü kullanırız. Peki bu keyword ile
amaçlanan nedir? Synchronized keywordü ile bir nesneyi kilitleriz ve 
synchronized bloğu tamamlananan kadar bir diğer thread tarafından erişim 
sağlanamaz. Bu sayede ilgili blokta bulunan kodlar multithread olmaktan çıkar
ve threadlar tarafından sırayla çalıştırılırlar.

O halde kodu tekrar düzenliyorum.

```
public class Singleton {

    private static Singleton instance;
    private String data; //Example data. It can be anything.

    private Singleton(String data){
        this.data = data;
    }

    public static Singleton getInstance(String data){

       synchronized (Singleton.class){
            if(instance == null){
                instance = new Singleton(data);
            }
        }
       return instance;
    }
}
```
Yukarıdaki kodda Singleton.class isimli nesneyi, yani doğrudan sınıfı kilitledik.
Böylece her thread sınfımıza sırayla erişecek. Fakat bu da verimsiz bir çözüm olacak.
Çünkü bu durum yalnızca bir defaya mahsus yaşanabileceği için, kalan tüm programın ömrü
boyunca buradaki yapıyı multithread olmaktan çıkarmış oluruz. 

Bunun önüne geçmek için synchronized yapısını bir if bloğu tarafından kapsülleyebiliriz;

```
public class Singleton {

    private static Singleton instance;
    private String data; //Example data. It can be anything.

    private Singleton(String data){
        this.data = data;
    }

    public static Singleton getInstance(String data){

       if(instance == null){
            synchronized (Singleton.class){
                if(instance == null){
                    instance = new Singleton(data);
                }
            }  
        }
       return instance;
    }
}
```
Yukarıdaki yapı **"double-checked locking (çift kontrollü kilitleme)"** olarak adlandırılır.

### "Double-Checked Locking" Nedir?
Bu yapı birden fazla thread tarafından paylaşılan bir nesnenin tekil bir örneğini oluştururken,
performansı arttırmak için kullanılır. Genellikle singleton yapılarında karşımıza çıkar.

Amaç threadler arasında paylaşılan nesnenin kilitlenmesinden önce oluşturulup
oluşturulmadığını kontrol etmektir. Bu sayede eğer nesne oluşturulmuşsa , nesneyi kilitlemeye
gerek kalmadan ilgili nesnenin referansı geriye döndürülür ve yeni instance yaratılmaz.

Fakat nesne henüz oluşturulmamışsa, synchronized bloğuna gireceği için nesne kilitlenir.
Bu blok içerisinde nesnenin varlığı kesin olarak kontrol edilir. Buradaki kesinliği sağlayan
nesnenin artık kilitlenmiş olmasıdır. Eğer nesne kesin olarak yaratılmamışsa nesneyi yaratır
ve synchronized bloğundan çıkar.

Fakat bu da başlı başına bir çözüm değildir. JVM tarafından yapılan optimizasyonlar nedeniyle
paylaşılan nesne tam olarak oluşturulmadan önce diğer nesneler tarafından erişilebilir.
Diyelim ki X thread'i nesnenin oluşturulmadığını fark edip nesneyi kilitledi. Ve yeni bir nesne
oluşturmaya başladı. Bu süreç JVM tarafında nesnenin oluşturulması sırasında nesneyi dışarı 
açabileceğinden, bir başka thread double-checking yapısının ilk kontrolünde nesnenin varlığını
görüp synchronized bloğuna girmeden boş veya hatalı bir nesne ile işlem yapabilir. Bu da 
uygulamanın hatalı çalışmasından tutun, uygulamanın çökmesine kadar bir çok soruna yol açabilir.

Bu noktada çözüm instanceyi **volatile** olarak tanımlamaktır.

```
public class Singleton {

    private static volatile Singleton instance;
    private String data; //Example data. It can be anything.

    private Singleton(String data){
        this.data = data;
    }

    public static Singleton getInstance(String data){

       if(instance == null){
            synchronized (Singleton.class){
                if(instance == null){
                    instance = new Singleton(data);
                }
            }  
        }
       return instance;
    }
}
```

Threadler double-checking yapısında 
bulunan nesnenin henüz tanımlanma aşamasında olduğunu fark edemeyebilir ve bu nesneye erişmeye
çalışabilir. Bu durumda beklenmeyen hatalar, istenmeyen sonuçlar ortaya çıkabilir.

**Volatile** keywordü kullanıldığında ise nesnenin okuma-yazma işlemleri sıralanır ve tamamlanmamış
bir nesneye erişim engellenir. Bu sayede tüm threadler için nesnenin tamamlanmış ve kullanıma, erişime
hazır olduğunu garanti eder. Ayrıca volatile olarak tanımlanan bir nesne işlemcinin ön belleğine
(cache) alınmaz, doğrudan bellekten okunur. Bu sayede nesnede yapılan bir değişikliğin diğer 
threadlar tarafından fark edilmesini sağlar ve veride tutarlılığı garantiler.

Bu işlemler ve önlemlerin ardından tasarım artık hazır. Güvenli ve multithread yapıya uygun 
bir biçime geldi. Fakat son bir düzeltmeye ihtiyacı var.

### Optimizasyon

**getInstance** metodu içerisinde volatile olarak tanımladığımız instance isimli nesneye iki kere
erişiyoruz. Birinci erişim if yapısı içerisinde kontrol ederken, ikinci erişim ise
geriye döndürürken. Burayı optimize etmek için metodun başında veriyi bir yerel değişkene alarak
erişim sayısını bire düşürebiliriz. Olması gereken tasarımın kodlarının son hali aşağıdadır.

```
public class Singleton {

    private static volatile Singleton instance;
    private String data; //Example data. It can be anything.

    private Singleton(String data){
        this.data = data;
    }

    public static Singleton getInstance(String data){
        Singleton localInstance = instance;
        if(localInstance == null){
            synchronized (Singleton.class){
                localInstance = instance;
                if(localInstance == null){
                    instance = localInstance = new Singleton(data);
                }
            }
        }
        return localInstance;
    }
}
```

@author M. Oğuzhan AYDOĞDU :)
