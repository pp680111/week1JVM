# Hello.java分析

源码

```
public class Hello {
    public int sum() {
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            if (i % 2 == 0) {
                sum += i;
            }
        }
        return sum;
    }
}
```

编译后的字节码文件，以javap -verbose执行输出

```
Classfile /G:/J2EE/Java训练营/week1JVM/out/production/01AnalyzeByteCode/Hello.class
  Last modified 2021年8月7日; size 417 bytes
  MD5 checksum 5b1232d6a7cc958b23b5a4462a150d89
  Compiled from "Hello.java"
public class Hello
  minor version: 0
  major version: 52
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #2                          // Hello
  super_class: #3                         // java/lang/Object
  interfaces: 0, fields: 0, methods: 2, attributes: 1
Constant pool:
   #1 = Methodref          #3.#18         // java/lang/Object."<init>":()V
   #2 = Class              #19            // Hello
   #3 = Class              #20            // java/lang/Object
   #4 = Utf8               <init>
   #5 = Utf8               ()V
   #6 = Utf8               Code
   #7 = Utf8               LineNumberTable
   #8 = Utf8               LocalVariableTable
   #9 = Utf8               this
  #10 = Utf8               LHello;
  #11 = Utf8               sum
  #12 = Utf8               ()I
  #13 = Utf8               i
  #14 = Utf8               I
  #15 = Utf8               StackMapTable
  #16 = Utf8               SourceFile
  #17 = Utf8               Hello.java
  #18 = NameAndType        #4:#5          // "<init>":()V
  #19 = Utf8               Hello
  #20 = Utf8               java/lang/Object
{
  public Hello();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 1: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   LHello;

  public int sum();
    descriptor: ()I
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=2, locals=3, args_size=1
         0: iconst_0
         1: istore_1
         2: iconst_1
         3: istore_2
         4: iload_2
         5: bipush        100
         7: if_icmpgt     26
        10: iload_2
        11: iconst_2
        12: irem
        13: ifne          20
        16: iload_1
        17: iload_2
        18: iadd
        19: istore_1
        20: iinc          2, 1
        23: goto          4
        26: iload_1
        27: ireturn
      LineNumberTable:
        line 3: 0
        line 4: 2
        line 5: 10
        line 6: 16
        line 4: 20
        line 9: 26
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            4      22     2     i   I
            0      28     0  this   LHello;
            2      26     1   sum   I
      StackMapTable: number_of_entries = 3
        frame_type = 253 /* append */
          offset_delta = 4
          locals = [ int, int ]
        frame_type = 15 /* same */
        frame_type = 250 /* chop */
          offset_delta = 5
}
SourceFile: "Hello.java"
```

按照输出的字节码信息中各方法的先后顺序分析每个方法的字节码。

## public Hello()

按照指令列表顺顺序来分析：

* aload_0表示从本地变量表中读取处第0个位置的一个引用，并将其压入操作数栈。从本地变量表可以看到，本地变量表位置0处是一个Hello类对象的引用，名称是this，也就是这里把this对戏那个的引用压入操作数栈
* invokespecial #1，常量池中#1是Object类的空构造方法，从操作数栈中弹出Hello对象的引用this，这一步操作就是调用this对象中的Object类的空构造方法。
* return，返回void

总结一下，这段字节码的意思就是获取当前对象的引用，调用从Object父类的空构造方法，然后返回。

##  public int sum()

按照指令列表的顺序分析一下各个指令的内容

1. iconst_0，把int类型值0推入操作数栈
2. istore_1，从操作数栈弹出一个int类型数值放入本地变量表第1位
3. iconst_1，把int类型值1推入操作数栈
4. istore_2，从操作数栈弹出一个int类型数值放入本地变量表第2位
5. iload_2，把本地变量表中第2位的int类型值读取出来放入操作数栈
6. bipush 100，把100这个int类型值推入栈中。这里的bi表示的是这个int值比较小，值范围在byte类型的-128~127之内，如果是大一点值范围，如short类型的-32768~32767时，使用的就是sipush
7. if_icmpgt 26，把操作数栈的前两位value1和value2弹出（这里先弹出的是value2），如果value1大于value2，则跳转到标号为26的操作指令去，否则执行下一条指令
8. iload_2
9. iconst_2，把int类型值2推入操作数栈
10. irem，把操作数栈的前两位value1和value2弹出（这里先弹出的是value2），然后计算value1对value2求余，余值放入操作数栈
11. ifne，从操作数栈弹出一位int值，如果不为0的话就跳转到标号20的操作指令去，否则执行下一条指令
12. iload_1
13. iload_2
14. iadd，把操作数栈的前两位弹出，计算他们的和，然后把结果放入操作数栈
15. istore_1
16. iinc 2,1，把本地变量表中的第2位的变量加上一个int类型的常量1
17. goto 4，跳转到标号为4的指令上执行
18. iload_1，把本地变量表中第1位的int类型值读取出来放入操作数栈
19. ireturn，从操作数栈中弹出一位int类型值，作为方法的返回值放入调用当前方法调用者的操作数栈中

按照这段指令的意思，在本地变量表的第1个位置放置一个值0，第二个位置放置一个值1，然后把判断第二位的值是否大于100，是的话就把返回本地变量表的第1个位置的值，否的话就对0求余，余数不为0时则把本地变量表第一个位置的值和第2个位置的值相加并放入本地变量表位置1，然后重复执行大于100的判断。

