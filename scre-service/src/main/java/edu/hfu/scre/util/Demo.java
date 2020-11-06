package edu.hfu.scre.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Demo {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("请输入数字");
		String tmp = input.nextLine();
		int n=Integer.parseInt(tmp);
		boolean[] lighting=new boolean[101];
		for(int i=1;i<101;i++) {
			lighting[i]=false;//false标识 灭 true 表示亮
		}
		
		for(int i=1;i<=n;i++) {
			for(int j=1;j<=100;j++) {
				if (j%i==0) {
					lighting[j]=!lighting[j];
				}
			}
		}
		//打印
		for(int i=1;i<101;i++) {
			if(lighting[i]) {
				System.out.println("亮着的灯为："+i);
			}
		}
		input.close();
	}
}
