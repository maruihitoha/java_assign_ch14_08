import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;

public class play_audio  extends JFrame{
	
	private Clip[] clip = new Clip[4];	// ����� Ŭ�� ��ü
	
	// ��� �޼���
	private JLabel message = new JLabel("üũ�� � ������� �ѹ� �����մϴ�.");
	
	// ����� ���� �̸���
	private String[] audio_name = {"audio/wolf.wav", "audio/dhol_drums.wav",
								"audio/sirenpolice.wav","audio/hiphop.wav"};
	
	// üũ�ڽ����� ���� �� ���������� ���� �̸����� ��� ��Ʈ�� �迭
	private String selected_audio[] = new String[4];
	
	// üũ�ڽ� 
	private JCheckBox[] waves = new JCheckBox[4];
	
	// ���� ���� ��ư, ���� ��ư
	private JButton btns[] = {new JButton("���� ����"), new JButton("���� ��") };
	
	private int now_playing = 0;
	private int cnt = 0;
	private boolean stop_flag = false;
	
	public play_audio()
	{
		super("����� ����");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(null);
		
		// ��� �޼��� ����
		message.setSize(450,20);
		message.setFont(new Font("����", Font.BOLD, 15));
		message.setHorizontalAlignment(JLabel.CENTER);
		message.setVerticalAlignment(JLabel.TOP);
		
		c.add(message);		
		
		// üũ �ڽ� ����
		for(int i = 0; i < waves.length; i++)
		{
			waves[i] = new JCheckBox(audio_name[i]);
			waves[i].setSize(450,20);
			waves[i].setLocation(140, 50 +(i*30));
			c.add(waves[i]);
		}
		
		// �׼� ������ ��ü ����
		MyActionListener al = new MyActionListener();
		
		// ��ư ����
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
				// ���� ���� ���� �� �뷡 ����
				case "���� ����":
					manage_audio();
					break;
					
				// ���� �� ���� �� ���� ��� ���� �뷡 ��ž.
				// ��ž �÷��� = true;
				case "���� ��":
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
		
		// üũ�� �׸� Ȯ�� �� ��� ����Ʈ�� ����.
		// selected audio = �������Ʈ
		for(int i = 0; i < waves.length; i++)
		{			
			if(waves[i].isSelected())
			{
				selected_audio[cnt++] = waves[i].getText();
				//System.out.println(selected_audio[cnt - 1]);
			}
			
		}
		
		// 0��° ���� ����
		load_audio(0);
				
	}
	
	
	public void load_audio(int i)
	{
		// ���� ������ �������� �� ���� ����� ���� ���� �ʱ�ȭ
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
				// ����� ������ ��θ��� ����
				File audioFile = new File(selected_audio[i]);
				
				// ����� ��Ʈ��
				final AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
				
				// Ŭ�� ��ü
				clip[i] = AudioSystem.getClip();
				
				// ���� ������� �뷡�� ��ȣ
				now_playing = i;

				// ���� ����� ���� ����
				clip[i].addLineListener(new LineListener() 
				{

					@Override
					public void update(LineEvent event) 
					{
						// TODO Auto-generated method stub
						if (event.getType() == LineEvent.Type.STOP) 
						{
							if (!stop_flag) // ���� �� ��ư���� ��ž�� ���� �ƴ϶��
							{
								try
								{
									// ���� ����� ��Ʈ���� �ݰ�, ���� ��ȣ�� ����Ϸ� ��
									audioStream.close();
									load_audio(i + 1);
								} catch (IOException e) 
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							// ���� �� ��ư���� ���� �� ���̶�� �׳� �����Ե�

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
