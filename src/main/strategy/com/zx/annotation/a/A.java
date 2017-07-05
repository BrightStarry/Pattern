package com.zx.annotation.a;
/**
 * 该类演示的是使用普通的策略模式来实现多种条件的判断，并执行不同的策略
 * 具体如下：
 * 历史购买总额  0-1000    9折  A
 *              1000-2000 8折  B
 *              >2000     7折  C
 */

/**
 * 测试
 */
public class A {
    public static void main(String[] args) {
        Customer customer = new Customer();
        //设置好两个金额
        customer.buy(1200.33,500.85);
        //输出结果
        System.out.println(customer.count());
    }
}

/**
 * 策略调用类
 */
class Customer {
    //历史购买总额
    private Double historyMoney;
    //本次购买金额
    private Double thisMoney;
    //策略接口
    private MoneyCount moneyCount;
    //用来模拟设置历史购买总额和本次购买金额,并根据历史总额设置不同的策略实现
    public void buy(Double historyMoney,Double thisMoney) {
        this.historyMoney = historyMoney;
        this.thisMoney = thisMoney;
        //使用简单工厂创建不同的策略实现类
        this.moneyCount = MoneyCountFactory.createMoneyCount(historyMoney);
    }
    //调用策略方法
    public Double count(){
        return moneyCount.count(thisMoney);
    }
}

/**
 * 策略接口 简单工厂
 */
class MoneyCountFactory {
    //根据历史总额创建不同的实现类
    public static MoneyCount createMoneyCount(Double historyMoney) {
        if (historyMoney < 1000) {
            return new AMoneyCout();
        } else if (historyMoney < 2000) {
            return new BMoneyCout();
        }
        return new CMoneyCout();
    }
}

/**
 * 策略接口
 */
interface MoneyCount {
    //使用本次购买总额，计算出应付金额(历史购买总额，通过调用不同的策略区分)
    Double count(Double thisMoney);
}

/**
 * A策略 历史0-1000
 */
class AMoneyCout implements MoneyCount {
    @Override
    public Double count(Double thisMoney) {
        //乘以折扣
        return thisMoney * 0.9;
    }
}
/**
 * B策略 历史1000-2000
 */
class BMoneyCout implements MoneyCount {
    @Override
    public Double count(Double thisMoney) {
        //乘以折扣
        return thisMoney * 0.8;
    }
}
/**
 * C策略 历史>2000
 */
class CMoneyCout implements MoneyCount {
    @Override
    public Double count(Double thisMoney) {
        //乘以折扣
        return thisMoney * 0.7;
    }
}
