package com.multicamp;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;
//CONTROLLER
public class MemoHandler implements ActionListener {
	MemoApp app; //VIEW
	MemoDAO dao=new MemoDAO(); //MODEL
	
	public MemoHandler (MemoApp app) {
		this.app=app;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o=e.getSource();
		if(o==app.btAdd) {
//			app.setTitle("Add"); //액션리스너 작동하는지 타이틀변경으로 확인용.
			addMemo();
		}
	}
	
	public void addMemo() {
		//[1]app의 tfName, tfMsg에 입력한 값 받아오기.
		String name=app.tfName.getText();
		String msg=app.tfMsg.getText();
		
		//[2]유효성 체크(null값, 빈문자열 체크)
		if(name!=null)
		app.showMessage("작성자를 입력해야 합니다");
		else if(msg!=null)
		app.showMessage("내용을 입력해야 합니다");

		//[3]1번에서 받아온 값을 MemoVO객체에 담아준다
		
		//[4]DAO의 insertMemo(vo)를 호출하고 그 결과값 받아온다.
		try {
		int n=dao.insertMemo(null);
		//[5]그 결과값에 따른 메시지 처리
		
		}catch(SQLException e) {
			app.showMessage(e.getMessage());
		}
	}
}
