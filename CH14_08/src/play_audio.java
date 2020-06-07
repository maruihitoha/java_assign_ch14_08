import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;

public class play_audio  extends JFrame{
	
	private Clip[] clip = new Clip[4];	// 재생용 클립 객체
	
	// 상단 메세지
	private JLabel message = new JLabel("체크된 곡만 순서대로 한번 연주합니다.");
	
	// 오디오 파일 이름들
	private String[] audio_name = {"audio/wolf.wav", "audio/dhol_drums.wav",
								"audio/sirenpolice.wav","audio/hiphop.wav"};
	
	// 체크박스에서 선택 된 음원파일의 파일 이름들을 담는 스트링 배열
	private String selected_audio[] = new String[4];
	
	// 체크박스 
	private JCheckBox[] waves = new JCheckBox[4];
	
	// 연주 시작 버튼, 종료 버튼
	private JButton btns[] = {new JButton("연주 시작"), new JButton("연주 끝") };
	
	private int now_playing = 0;
	private int cnt = 0;
	private boolean stop_flag = false;
	
	public play_audio()
	{
		super("오디오 연주");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(null);
		
		// 상단 메세지 설정
		message.setSize(450,20);
		message.setFont(new Font("굴림", Font.BOLD, 15));
		message.setHorizontalAlignment(JLabel.CENTER);
		message.setVerticalAlignment(JLabel.TOP);
		
		c.add(message);		
		
		// 체크 박스 설정
		for(int i = 0; i < waves.length; i++)
		{
			waves[i] = new JCheckBox(audio_name[i]);
			waves[i].setSize(450,20);
			waves[i].setLocation(140, 50 +(i*30));
			c.add(waves[i]);
		}
		
		// 액션 리스너 객체 생성
		MyActionListener al = new MyActionListener();
		
		// 버튼 설정
		for(int i = 0; i < btns.length; i++)
		{
			btns[i].setSize(120,40);
			btns[i].setLocation(90 + (i*150), 220);
			c.add(btns[i]);
			btns[i].addActionListener(al);
		}
		
		
		setSize(450,300);
		setVisible(true);
		
		
	}
	
	class MyActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			switch(e.getActionCommand())
			{
				// 연주 시작 누를 시 노래 시작
				case "연주 시작":
					manage_audio();
					break;
					
				// 연주 끝 누를 시 현재 재생 중인 노래 스탑.
				// 스탑 플래그 = true;
				case "연주 끝":
					clip[now_playing].stop();
					stop_flag = true;
					break;
			}
			
		}
		
	}
	
	
	public void manage_audio()
	{
		
		cnt = 0;
		stop_flag = false;
		
		// 체크된 항목 확인 후 재생 리스트에 넣음.
		// selected audio = 재생리스트
		for(int i = 0; i < waves.length; i++)
		{			
			if(waves[i].isSelected())
			{
				selected_audio[cnt++] = waves[i].getText();
				//System.out.println(selected_audio[cnt - 1]);
			}
			
		}
		
		// 0번째 부터 시작
		load_audio(0);
				
	}
	
	
	public void load_audio(int i)
	{
		// 만약 끝까지 연주했을 시 다음 재생을 위해 설정 초기화
		if(i == cnt)
		{
			cnt = 0;
			for(int a = 0; a < selected_audio.length; a++)
			{
				selected_audio[a] = "";
				clip[a].close();
			}
					
		}
		else 
		{
			try 
			{
				// 오디오 파일의 경로명을 받음
				File audioFile = new File(selected_audio[i]);
				
				// 오디오 스트림
				final AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
				
				// 클립 객체
				clip[i] = AudioSystem.getClip();
				
				// 현재 재생중인 노래의 번호
				now_playing = i;

				// 음원 종료시 다음 동작
				clip[i].addLineListener(new LineListener() 
				{

					@Override
					public void update(LineEvent event) 
					{
						// TODO Auto-generated method stub
						if (event.getType() == LineEvent.Type.STOP) 
						{
							if (!stop_flag) // 연주 끝 버튼으로 스탑된 것이 아니라면
							{
								try
								{
									// 현재 오디오 스트림을 닫고, 다음 번호를 재생하러 감
									audioStream.close();
									load_audio(i + 1);
								} catch (IOException e) 
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							// 연주 끝 버튼으로 종료 된 것이라면 그냥 끝나게됨

						}

					}
				});

				clip[i].open(audioStream);
				clip[i].start();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new play_audio();
	}

}
