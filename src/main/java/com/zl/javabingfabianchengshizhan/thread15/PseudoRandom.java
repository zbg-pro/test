package com.zl.javabingfabianchengshizhan.thread15;

/**
 * @author Allen.zhang
 * @title: PseudoRandom
 * @projectName zl
 * @description: TODO
 * @date 2022/5/1115:28
 */
public class PseudoRandom {

    int calculateNext(int prev) {
        prev ^= prev <<6;
        prev ^= prev >> 21;
        prev ^= prev << 7;
        return prev;
    }

    public static void main(String[] args) {
        //运算符 说明：两个数都转为二进制，然后从高位开始比较，
        // |= 如果两个数都为1则为1，否则为0
        // &= 如果两个数都为1则为1， 否则为0
        // ^= 如果两个数相同则为1，否则为0
        // ~ 如果位为0，结果是1，如果位为1，结果是0

        //<< 、>> 说明
        //<< 有符号左移 相当于乘以2n
        //>> 有符号右移 相当于除2n
        int aa = 5;
        aa = aa>>1;
        System.out.println("aa " + aa);

        //>>> 无符号右移动 >>>为逻辑移位符，向右移n位，高位补0
        //无论该数为整数还是负数，右移后最高位都补0
        // 由于计算机中存储的都是数的补码，正数的原码、反码、补码都是相同的；
        // 而负数的原码、反码、补码是不一样的，补码=原码取反+1（符号位不变）。
        // 所以，负数是按照它的补码输出的。

        //十进制转2进制
        String rs = decimal2Binary(31);
        String result = Integer.toBinaryString(-31);
        System.out.println("rs: " + rs);
        System.out.println("rs2:" + result);
        //原码，反码，补码

        int a = 10;
        a ^= a+1;
        //a+=1;
        System.out.println(a);
    }


    public static String decimal2Binary(int scanner){
        String base="";
        int sys=0;
        while(true){
            sys=scanner%2;
            scanner=scanner/2;
            base=sys+base;
            if(scanner<2){
                sys=scanner%2;
                base=sys+base;
                break;
            }
        }
        return base;
    }

    public static void yuanmaBumaFanma(){
        /*
        下面给出几个示例：

        40：

        原码：00101000
        反码：00101000
        补码：00101000
        1
        2
        3
        -216：

        原码：1000000011011000
        反码：1111111100100111
        补码：1111111100101000
        1
        2
        3
        -107：

        原码：11101011
        反码：10010100
        补码：10010101
        可以看到，对于正数，其原码、反码、补码相同。对于负数，原码中最高位用来表示符号，反码就是除了最高位外，其余位取反，补码就是反码+1

        为什么要设计补码
        上面介绍了原码、反码和补码三者的概念，那么，计算机中为什么要设计补码这一概念呢？因为直接用原码涉及到减法操作，这就增加了计算机底层电路涉及的复杂性。而用补码操作时，当减去一个数时，可以看做加上一个负数，然后转变位加上这个负数的补码。即: 1-1 = 1 + (-1) = 0 , 所以机器可以只有加法而没有减法, 这样计算机运算的设计就更简单了.

        而使用补码的好处还有，在计算中可以直接带上符号位进行计算，比如计算
        版权声明：本文为CSDN博主「JermeryBesian」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
        原文链接：https://blog.csdn.net/Urbanears/article/details/115740508
    */



    }


}
