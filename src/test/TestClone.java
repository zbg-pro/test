/**
 * Created by hl on 2018/6/7.
 */
public class TestClone implements Cloneable {

    private Integer id;

    private String name;

    private TestSubCls testSubCls;

    public static String aa;

    public TestSubCls getTestSubCls() {
        return testSubCls;
    }

    public void setTestSubCls(TestSubCls testSubCls) {
        this.testSubCls = testSubCls;
    }

    public TestClone(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestClone{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


    public static void main(String[] args) throws CloneNotSupportedException {
        TestClone t1 = new TestClone(12, "zl23");

        TestSubCls ts = new TestSubCls("england");
        t1.setTestSubCls(ts);

        TestClone t2 = (TestClone) t1.clone();

        TestClone t3 = t1;

        System.out.println(t1 == t2);

        System.out.println(t1 == t3);

        System.out.println(t1.getTestSubCls() == t2.getTestSubCls());//如果作为嵌套对象，会出现内部没有clone,无论内部对象是否进行了clone接口实现，仅仅对于基本类型进行了clone
        //对象只是修改了一个不同的引用，其实在计算机中还是指向的同一个地址

        //需要注意：String作为一个对象，也实现了深度copy，
        //但StringBuffer没有实现，需要我们加入类似重新接口实现clone方法，成员变量对象 = 成员变量对象.CLONE():

        /**
         * Object  o = super.clone();
         * o.subAttr = super.clone()
         */


    }

}

class TestSubCls implements Cloneable{
    String address;

    public TestSubCls(String address){
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "TestSubCls{" +
                "address='" + address + '\'' +
                '}';
    }
}