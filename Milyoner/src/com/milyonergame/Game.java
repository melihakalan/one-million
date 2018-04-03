package com.milyonergame;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.startapp.android.publish.StartAppAd;

public class Game extends Activity
{
	private StartAppAd startAppAd = new StartAppAd(this);
	
	private static QuestionDB QDB = new QuestionDB();
	private ArrayList<Integer> QList = new ArrayList<Integer>();
	private static final int iMoneyList[] = {500, 1000, 2000, 3000, 5000, 7500, 15000, 30000, 60000, 125000, 250000, 1000000};
	
	private static LinearLayout lLeft, lRight;
	private static TextView tMoney[] = new TextView[12];
	private static TextView tQuestion, tAnswerA, tAnswerB, tAnswerC, tAnswerD;
	private static ImageView jSeyirci, jTelefon, jYari, j2x;
	private static Button btnLeave;
	
	private boolean b_jSeyirci = true, b_jTelefon = true, b_jYari = true, b_j2x = true;
	private boolean bUsed2X = false;
	
	private static final int cWhite = Color.rgb(0xD8,0xD8,0xD8); 
	private static final int cYellow = Color.rgb(0xDB,0xA9,0x01); 
	private static final int cGreen = Color.rgb(0x40,0xFF,0x0); 
	
	private boolean bStarted = false;
	private int iLevel = 0;
	private int iEarned = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		lLeft = (LinearLayout) findViewById(R.id.game_left);
		lRight = (LinearLayout) findViewById(R.id.game_right);
		
		tQuestion = (TextView) findViewById(R.id.game_question);
		tAnswerA = (TextView) findViewById(R.id.game_answerA);
		tAnswerB = (TextView) findViewById(R.id.game_answerB);
		tAnswerC = (TextView) findViewById(R.id.game_answerC);
		tAnswerD = (TextView) findViewById(R.id.game_answerD);
		
		jSeyirci = (ImageView) findViewById(R.id.game_jseyirci);
		jTelefon = (ImageView) findViewById(R.id.game_jtelefon);
		jYari = (ImageView) findViewById(R.id.game_jyari);
		j2x = (ImageView) findViewById(R.id.game_j2x);
		
		btnLeave = (Button) findViewById(R.id.game_leave);
		
		tMoney[0] = (TextView) findViewById(R.id.game_money1);
		tMoney[1] = (TextView) findViewById(R.id.game_money2);
		tMoney[2] = (TextView) findViewById(R.id.game_money3);
		tMoney[3] = (TextView) findViewById(R.id.game_money4);
		tMoney[4] = (TextView) findViewById(R.id.game_money5);
		tMoney[5] = (TextView) findViewById(R.id.game_money6);
		tMoney[6] = (TextView) findViewById(R.id.game_money7);
		tMoney[7] = (TextView) findViewById(R.id.game_money8);
		tMoney[8] = (TextView) findViewById(R.id.game_money9);
		tMoney[9] = (TextView) findViewById(R.id.game_money10);
		tMoney[10] = (TextView) findViewById(R.id.game_money11);
		tMoney[11] = (TextView) findViewById(R.id.game_money12);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		enableAnswers(false);
		highlightPanel();
	}
	
	private void highlightPanel()
	{
		new CountDownTimer(5000, 300)
		{
			private int i = 0;
			public void onTick(long ms)
			{
				if(i > 11)
				{
					tMoney[11].setTextColor(cWhite);
					Log.d("Debug", "Highlight cancelled.");
					return;
				}
				
				tMoney[i].setTextColor(cGreen);
				if(i > 0)
				{
					if(i == 2 || i == 7)
						tMoney[i-1].setTextColor(cWhite);
					else
						tMoney[i-1].setTextColor(cYellow);
				}
				i++;
			}

			public void onFinish()
			{
				Log.d("Debug", "Highlight finished.");
				startGame();
			}
		}.start();		
	}
	
	private void startGame()
	{
		lLeft.setVisibility(LinearLayout.VISIBLE);
		tMoney[0].setTextColor(cGreen);
		this.bStarted = true;
		enableAnswers(true);
		
		getNewQuestion();
	}
	
	private void getNewQuestion()
	{
		final int max = QDB.getCount();
		
		int ridx;
		do
		{
			ridx = new Random().nextInt(max);
		}
		while( QList.contains(ridx) );
		
		tQuestion.setText( QDB.getQuestion(ridx) );
		tAnswerA.setText( "A) " + QDB.getAnswer(ridx, 0) );
		tAnswerB.setText( "B) " + QDB.getAnswer(ridx, 1) );
		tAnswerC.setText( "C) " + QDB.getAnswer(ridx, 2) );
		tAnswerD.setText( "D) " + QDB.getAnswer(ridx, 3) );
			
		QList.add(ridx);
	}
	
	public void selectAnswerA(View v)
	{
		enableAnswers(false);
		tAnswerA.setBackgroundResource(R.drawable.shape_selected);
		confirmAnswer(0);
	}
	
	public void selectAnswerB(View v)
	{
		enableAnswers(false);
		tAnswerB.setBackgroundResource(R.drawable.shape_selected);
		confirmAnswer(1);
	}
	
	public void selectAnswerC(View v)
	{
		enableAnswers(false);
		tAnswerC.setBackgroundResource(R.drawable.shape_selected);
		confirmAnswer(2);
	}
	
	public void selectAnswerD(View v)
	{
		enableAnswers(false);
		tAnswerD.setBackgroundResource(R.drawable.shape_selected);
		confirmAnswer(3);
	}
	
	private void confirmAnswer(final int answeridx)
	{
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
	    dlgAlert.setTitle("Cevap"); 
	    dlgAlert.setMessage("Son kararýnýz mý?"); 
	    
	    dlgAlert.setPositiveButton("Evet",new DialogInterface.OnClickListener()
	    {
	        public void onClick(DialogInterface dialog, int whichButton)
	        {
	        	dialogConfirmed(answeridx);
	        }
	   });
	    
	    dlgAlert.setNegativeButton("Hayýr", new DialogInterface.OnClickListener()
	    {
	        public void onClick(DialogInterface dialog, int whichButton)
	        {
	        	switch(answeridx)
	        	{
	        	case 0:
	        		tAnswerA.setBackgroundResource(R.drawable.shape);
	        		break;
	        	case 1:
	        		tAnswerB.setBackgroundResource(R.drawable.shape);
	        		break;
	        	case 2:
	        		tAnswerC.setBackgroundResource(R.drawable.shape);
	        		break;
	        	case 3:
	        		tAnswerD.setBackgroundResource(R.drawable.shape);
	        		break;
	        	}
	        	enableAnswers(true);
	        	dialog.cancel();
	        }
	   });
	    
	    dlgAlert.setCancelable(false);
	    dlgAlert.create().show();
	}
	
	private void dialogConfirmed(final int answeridx)
	{
		final int qidx = QList.get( QList.size() - 1 );
		final boolean bCorrect = QDB.checkAnswer(qidx, answeridx);
		
		int rndtime = new Random().nextInt(3000) + 5000;
		new CountDownTimer(rndtime, rndtime)
		{
			@Override
			public void onTick(long millisUntilFinished) {}
			
			@Override
			public void onFinish()
			{
				if(bCorrect)
					wonLevel(answeridx);
				else
					lostLevel(answeridx);
			}
		}.start();
	}
	
	private void wonLevel(final int answeridx)
	{
		Toast.makeText(Game.this, "Tebrikler !!!", Toast.LENGTH_LONG).show();
		
		TextView _tAnswer = null;
		switch(answeridx)
		{
		case 0:
			_tAnswer = tAnswerA;
			break;
		case 1:
			_tAnswer = tAnswerB;
			break;
		case 2:
			_tAnswer = tAnswerC;
			break;
		case 3:
			_tAnswer = tAnswerD;
			break;
		}
		
		final TextView tAnswer = _tAnswer;
		tAnswer.setBackgroundResource(R.drawable.shape_won);
		
		new CountDownTimer(8000, 500)
		{
			private boolean bGreen = true;
			@Override
			public void onTick(long ms)
			{
				if(ms <= 4000)
				{
					tAnswer.setBackgroundResource(R.drawable.shape_won);
					return;
				}
				
				bGreen = !bGreen;
				if(bGreen)
					tAnswer.setBackgroundResource(R.drawable.shape_won);
				else
					tAnswer.setBackgroundResource(R.drawable.shape);
			}
			
			@Override
			public void onFinish()
			{
				tAnswer.setBackgroundResource(R.drawable.shape);
				nextLevel();
			}
		}.start();
	}
	
	private void nextLevel()
	{	
		iLevel++;
		
		if(iLevel == 12)
		{
			showDialog(Game.this, "Tebrikler!", "1.000.000 TL kazandiniz!");
			return;
		}
		
		if(iLevel == 7)
		{
			j2x.setVisibility(ImageView.VISIBLE);
			Toast.makeText(Game.this, "Çift seçenek jokeriniz eklendi!", Toast.LENGTH_LONG).show();
		}
		
		enableAnswers(true);
		
		tAnswerA.setBackgroundResource(R.drawable.shape);
		tAnswerB.setBackgroundResource(R.drawable.shape);
		tAnswerC.setBackgroundResource(R.drawable.shape);
		tAnswerD.setBackgroundResource(R.drawable.shape);
		
		if(iLevel == 2 || iLevel == 7)
			tMoney[iLevel - 1].setTextColor(cWhite);
		else
			tMoney[iLevel - 1].setTextColor(cYellow);
		
		tMoney[iLevel].setTextColor(cGreen);
		
		getNewQuestion();
	}
	
	private void lostLevel(int answeridx)
	{
		final int qidx = QList.get( QList.size() - 1 );
		final int correctidx = QDB.getCorrectAnswer(qidx);
		
		switch(answeridx)
		{
		case 0:
			tAnswerA.setBackgroundResource(R.drawable.shape_lost);
			break;
		case 1:
			tAnswerB.setBackgroundResource(R.drawable.shape_lost);
			break;
		case 2:
			tAnswerC.setBackgroundResource(R.drawable.shape_lost);
			break;
		case 3:
			tAnswerD.setBackgroundResource(R.drawable.shape_lost);
			break;
		}
		
		if(bUsed2X)
		{
			bUsed2X = false;
			enableAnswers(true);
			return;
		}
		
		switch(correctidx)
		{
		case 0:
			tAnswerA.setBackgroundResource(R.drawable.shape_won);
			break;
		case 1:
			tAnswerB.setBackgroundResource(R.drawable.shape_won);
			break;
		case 2:
			tAnswerC.setBackgroundResource(R.drawable.shape_won);
			break;
		case 3:
			tAnswerD.setBackgroundResource(R.drawable.shape_won);
			break;
		}
		
		if(iLevel > 6)
			iEarned = iMoneyList[6];
		else if(iLevel > 1)
			iEarned = iMoneyList[1];
		else
			iEarned = 0;
		
		new CountDownTimer(4000, 4000)
		{
			@Override
			public void onTick(long millisUntilFinished) {}
			
			@Override
			public void onFinish()
			{
				showDialog(Game.this, "Kaybettiniz!", "Yanlýþ cevap!\nKazandýðýnýz ödül: " + String.valueOf(iEarned) + " TL");
			}
		}.start();
	}
	
	private void enableAnswers(boolean bEnable)
	{
		tAnswerA.setClickable(bEnable);
		tAnswerB.setClickable(bEnable);
		tAnswerC.setClickable(bEnable);
		tAnswerD.setClickable(bEnable);
		
		jSeyirci.setClickable(bEnable);
		jTelefon.setClickable(bEnable);
		jYari.setClickable(bEnable);
		j2x.setClickable(bEnable);
		
		btnLeave.setClickable(bEnable);
	}
	
	private void showDialog(Context context, String title, String msg)
	{
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);                      
	    dlgAlert.setTitle(title); 
	    dlgAlert.setMessage(msg); 
	    dlgAlert.setPositiveButton("Tamam",new DialogInterface.OnClickListener()
	    {
	        public void onClick(DialogInterface dialog, int whichButton)
	        {
	        	dialog.cancel();
	        	finish();
	        }
	   });
	    dlgAlert.setCancelable(false);
	    dlgAlert.create().show();
	}
	
	public void leaveGame(View v)
	{
		if(iLevel == 0)
			iEarned = 0;
		else
			iEarned = iMoneyList[iLevel];
		
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Game.this);                      
	    dlgAlert.setTitle("Çekil"); 
	    dlgAlert.setMessage("Çekilmek istiyor musunuz?"); 
	    dlgAlert.setPositiveButton("Evet",new DialogInterface.OnClickListener()
	    {
	        public void onClick(DialogInterface dialog, int whichButton)
	        {
	        	showDialog(Game.this, "Tebrikler", "Yarýþmadan çekildiniz.\nKazandýðýnýz ödül: " + String.valueOf(iEarned) + " TL");
	        }
	   });
	    
	    dlgAlert.setNegativeButton("Hayýr", new DialogInterface.OnClickListener()
	    {
	        public void onClick(DialogInterface dialog, int whichButton)
	        {
	        	dialog.cancel();
	        }
	   });
	    
	    dlgAlert.setCancelable(false);
	    dlgAlert.create().show();	
	}	
	
	public void useGuests(View v)
	{
		if(!b_jSeyirci)
			return;
		
		final int qidx = QList.get( QList.size() - 1 );
		final int correctidx = QDB.getCorrectAnswer(qidx);
		
		int pcorrect = new Random().nextInt(25) + 60;
		int pwrong1 = new Random().nextInt(100 - pcorrect) + 1;
		int pwrong2 = new Random().nextInt(100 - pcorrect - pwrong1) + 1;
		int pwrong3 = 100 - pcorrect - pwrong1 - pwrong2;
		
		String MSG = "Seyirci jokeri:\n";
		if(correctidx == 0)
			MSG += "A) %" + pcorrect + "\nB) %" + pwrong1 + "\nC) %" + pwrong2 + "\nD) %" + pwrong3;
		else if(correctidx == 1)
			MSG += "A) %" + pwrong1 + "\nB) %" + pcorrect + "\nC) %" + pwrong2 + "\nD) %" + pwrong3;
		else if(correctidx == 2)
			MSG += "A) %" + pwrong1 + "\nB) %" + pwrong2 + "\nC) %" + pcorrect + "\nD) %" + pwrong3;
		else if(correctidx == 3)
			MSG += "A) %" + pwrong1 + "\nB) %" + pwrong2 + "\nC) %" + pwrong3 + "\nD) %" + pcorrect;
			
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Game.this);                      
	    dlgAlert.setTitle("Joker"); 
	    dlgAlert.setMessage(MSG); 
	    dlgAlert.setPositiveButton("Tamam",new DialogInterface.OnClickListener()
	    {
	        public void onClick(DialogInterface dialog, int whichButton)
	        {
	        	dialog.cancel();
	        	jSeyirci.setBackgroundResource(R.drawable.j_seyirci_on);
	        	b_jSeyirci = false;
	        }
	   });
	    dlgAlert.setCancelable(true);
	    dlgAlert.create().show();
	}
	
	public void usePhone(View v)
	{
		if(!b_jTelefon)
			return;
		
		final int qidx = QList.get( QList.size() - 1 );
		final int correctidx = QDB.getCorrectAnswer(qidx);	
		
		String MSG = null;
		int rand = new Random().nextInt(4);
		if(rand == 0)
			MSG = "Telefon:\nMalesef, bu soru hakkýnda fikrim yok.";
		else if(rand == 1)
		{
			int wrongidx;
			do
			{
				wrongidx = new Random().nextInt(4);
			}while(wrongidx == correctidx);
			
			if(wrongidx == 0)
				MSG = "Telefon:\nEmin deðilim fakat cevabýn A þýkký olduðunu düþünüyorum...";
			else if(wrongidx == 1)
				MSG = "Telefon:\nEmin deðilim fakat cevabýn B þýkký olduðunu düþünüyorum...";
			else if(wrongidx == 2)
				MSG = "Telefon:\nEmin deðilim fakat cevabýn C þýkký olduðunu düþünüyorum...";
			else if(wrongidx == 3)
				MSG = "Telefon:\nEmin deðilim fakat cevabýn D þýkký olduðunu düþünüyorum...";
		}
		else if(rand == 2 || rand == 3)
		{
			if(correctidx == 0)
				MSG = "Telefon:\nCevabýn A þýkký olduðunu düþünüyorum.";
			else if(correctidx == 1)
				MSG = "Telefon:\nCevabýn B þýkký olduðunu düþünüyorum.";
			else if(correctidx == 2)
				MSG = "Telefon:\nCevabýn C þýkký olduðunu düþünüyorum.";
			else if(correctidx == 3)
				MSG = "Telefon:\nCevabýn D þýkký olduðunu düþünüyorum.";
		}
		
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Game.this);                      
	    dlgAlert.setTitle("Joker"); 
	    dlgAlert.setMessage(MSG); 
	    dlgAlert.setPositiveButton("Tamam",new DialogInterface.OnClickListener()
	    {
	        public void onClick(DialogInterface dialog, int whichButton)
	        {
	        	dialog.cancel();
	        	jTelefon.setBackgroundResource(R.drawable.j_telefon_on);
	        	b_jTelefon = false;
	        }
	   });
	    dlgAlert.setCancelable(true);
	    dlgAlert.create().show();
	}
	
	public void useHalf(View v)
	{
		if(!b_jYari)
			return;
		
		final int qidx = QList.get( QList.size() - 1 );
		final int correctidx = QDB.getCorrectAnswer(qidx);	
		
		int wrong1, wrong2;
		
		do
		{
			wrong1 = new Random().nextInt(4);
		}while(wrong1 == correctidx);
		
		do
		{
			wrong2 = new Random().nextInt(4);
		}while(wrong2 == correctidx || wrong2 == wrong1);
		
		if(wrong1 == 0)
		{
			tAnswerA.setText(null);
			tAnswerA.setClickable(false);
		}
		else if(wrong1 == 1)
		{
			tAnswerB.setText(null);
			tAnswerB.setClickable(false);	
		}
		else if(wrong1 == 2)
		{
			tAnswerC.setText(null);
			tAnswerC.setClickable(false);	
		}
		else if(wrong1 == 3)
		{
			tAnswerD.setText(null);
			tAnswerD.setClickable(false);	
		}
		
		if(wrong2 == 0)
		{
			tAnswerA.setText(null);
			tAnswerA.setClickable(false);
		}
		else if(wrong2 == 1)
		{
			tAnswerB.setText(null);
			tAnswerB.setClickable(false);	
		}
		else if(wrong2 == 2)
		{
			tAnswerC.setText(null);
			tAnswerC.setClickable(false);	
		}
		else if(wrong2 == 3)
		{
			tAnswerD.setText(null);
			tAnswerD.setClickable(false);	
		}
		
		jYari.setBackgroundResource(R.drawable.j_yari_on);
		b_jYari = false;
	}
	
	public void use2X(View v)
	{
		if(!b_j2x)
			return;
		
		bUsed2X = true;
		
		j2x.setBackgroundResource(R.drawable.j_2x_on);
		b_j2x = false;
	}
}
