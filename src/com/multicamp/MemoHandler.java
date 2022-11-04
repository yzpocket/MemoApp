package com.multicamp;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.List;
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
			app.clearTf();
			listMemo();
		}else if(o==app.btList) {
			listMemo();
		}else if(o==app.btDel) {
			deleteMemo();
		}
	}
	
	//DELETE문 메서드
	public void deleteMemo() {
		String idxStr=app.showInput("삭제할 글 번호를 입력하세요");
		if(idxStr==null) return;
		int n=dao.deleteMemo(Integer.parseInt(idxStr.trim()));
		String str=(n>0)?"글 삭제 성공":"글 삭제 실패";
		app.showMessage(str);
	}
	
	//SELECT문 메서드
	public void listMemo(){ 
		try{
			List<MemoVO> arr=dao.selectMemoAll();
		//반복문 면서 MemoVO를 꺼내고
		//ta에 꺼낸값 출력하기.
			app.setTitle("글 개수 : " +arr.size());
		}catch(SQLException e) {
			e.printStackTrace();
			app.showMessage(e.getMessage());
		}
	}
	
	//INSERT문 메서드
	public void addMemo() {  
		//[1]app의 tfName, tfMsg에 입력한 값 받아오기.
		String name=app.tfName.getText();
		String msg=app.tfMsg.getText();
		
		//[2]유효성 체크(null값, 빈문자열 체크)
		if(name==null || name.trim().isEmpty()) {
			app.showMessage("작성자를 입력해야 합니다");
			app.tfName.requestFocus();
			return;
		}
		if(msg==null || msg.trim().isEmpty()) {
			app.showMessage("내용을 입력해야 합니다");
			app.tfMsg.requestFocus();
			return;
		}

		//[3]1번에서 받아온 값을 도메인객체인 MemoVO객체에 담아준다
        MemoVO vo=new MemoVO(0, name, msg, null);
        				// 글번호는 시퀀스로할거라 그냥0, 작성일도 sysdate쓸꺼라 null값
		//[4]DAO의 insertMemo(vo)를 호출하고 그 결과값(int) 받아온다.
        try {
            int i = dao.insertMemo(vo);
            //[5] 결과값에 따른 메세지 처리
            if(i>0) app.setTitle("메모 등록 완료!!");
            else app.showMessage("메모 등록 실패");
        }            
		//[5]그 결과값에 따른 메시지 처리
		 catch(SQLException e) {
			app.showMessage(e.getMessage());
		}
	}
}
