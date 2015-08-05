//package com.hash_soft.browser;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.widget.TextView;
//
//
//public class Test0416 extends Activity {
//
//    TextView textView;
//    Context c;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test0416);
//        textView = (TextView)findViewById(R.id.textView);
//        c = this;
//        Student st = new Student(this, "이름이닷 데헷",1,5,14,true);
//        textView.setText(st.getName()+"\n"+st.getGrade()+"학년\n"+st.getBan()+"반\n"+st.getNum()+"번\n"+st.getIsMan());
//
//        if(st.getIsMan()==true) textView.setTextColor(Color.parseColor("#F43724"));
//        else textView.setTextColor(Color.parseColor("#3c3c3c"));
//    }
//
//    class Student{
//        String name;
//        int grade, ban, num;
//        boolean isMan;
//
//        public Student(Context c, String name, int grade, int ban, int num, boolean isMan){
//            this.name=name;
//            this.grade=grade;
//            this.ban=ban;
//            this.num=num;
//            this.isMan=isMan;
//        }
//        public String getName() {
//            return this.name;
//        }
//
//        public int getGrade() {
//            return this.grade;
//        }
//        public int getBan(){
//            return this.ban;
//        }
//
//        public int getNum(){
//            return this.num;
//        }
//
//        public Boolean getIsMan(){
//            return this.isMan;
//        }
//    }
//}