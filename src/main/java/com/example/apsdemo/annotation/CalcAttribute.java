/*
 * @author     ：root
 * @date       ：Created in 2021/3/3 17:10
 */
package com.example.apsdemo.annotation;

public @interface CalcAttribute {

    Class target();

    String route();

    String attribute();
}
