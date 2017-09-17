package com.prototype.helpkiosk.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.jdesktop.swingworker.SwingWorker;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.prototype.helpkiosk.instruction.InstructionSingleton;

class LiveView extends JPanel implements ActionListener {
	private IDevice device;
	private GetScreenshotTask task;
	private BufferedImage image;
	private volatile boolean isLoading;
	private Crosshair crosshair;
	private Timer timer;
	private int refreshRate = 500;
	private InstructionSingleton instructionSingleton = InstructionSingleton.getInstance();
	private int width, height;

	LiveView (IDevice device) {
		setLayout(new FlowLayout());
		setOpaque(true);
		setBackground(Color.WHITE);

		this.device = device;
		this.width = 240;
		this.height = 400;

		this.timer = new Timer(refreshRate, this);
		this.timer.setInitialDelay(0);
		this.timer.setRepeats(true);

		JPanel panel = buildViewerAndControls();
		add(panel);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LiveView.this.timer.start();
			}
		});

	}

	private JPanel buildViewerAndControls() {
		JPanel panel = new JPanel();

		Crosshair viewPanel = new Crosshair(new ScreenshotViewer());
		viewPanel.setBackground(Color.WHITE);
		viewPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		viewPanel.setBounds(0, 0, width, height);

		JLabel title = new JLabel("<html><br></html>");
		title.setFont(new Font("Helvetica", Font.BOLD, 23));
		title.setForeground(new Color(0x3B70A3));

		//ImageIcon img = new ImageIcon("img/nexusoneinhandblur.png");
		ImageIcon img = new ImageIcon("img/nexusoneinhand.png");

		JLabel bg = new JLabel(img);
		bg.setBackground(Color.WHITE);
		bg.setLayout(new BoxLayout(bg, BoxLayout.Y_AXIS));
		JPanel rigid = new JPanel();
		rigid.setOpaque(false);
		rigid.setLayout(new BoxLayout(rigid, BoxLayout.X_AXIS));
		rigid.setPreferredSize(new Dimension(100, 30));
		rigid.add(title);
		bg.add(rigid);

		JPanel rigid2 = new JPanel();
		rigid2.setOpaque(false);
		rigid2.setPreferredSize(new Dimension(92, height));

		JPanel rigid3 = new JPanel();
		rigid3.setOpaque(false);
		rigid3.setPreferredSize(new Dimension(10, height));

		JPanel filler = new JPanel();
		filler.setLayout(new BoxLayout(filler, BoxLayout.X_AXIS));
		filler.setOpaque(false);
		filler.add(rigid2);
		filler.add(viewPanel);
		filler.add(rigid3);

		JPanel rigid4 = new JPanel();
		rigid4.setLayout(null);
		rigid4.setOpaque(false);
		rigid4.setPreferredSize(new Dimension(width, 100));   
		instructionSingleton.setLowerContainer(rigid4);

		bg.add(filler);
		bg.add(rigid4);

		panel.add(bg);
		return panel;
	}

	void stop() {
		this.timer.stop();
	}

	void start() {
		this.timer.start();
	}

	public void actionPerformed(ActionEvent event) {
		// if no current task, start a new GetScreenshot task
		if ((this.task != null) && (!this.task.isDone())) {
//			System.out.println("Existing task, no new one");
			return;
		}
		System.out.println("New screenshot task created!");
		this.task = new GetScreenshotTask();
		this.task.execute();
	}

	private class GetScreenshotTask extends SwingWorker<Boolean, Void> {
		// SwingWorkers are essentially new threads, creating a new one adds a new task to a free worker thread
		
		private GetScreenshotTask() {
			
		}

		protected Boolean doInBackground() throws Exception {
			RawImage rawImage;
			try {				
				// Method #1: original implementation
				// takes too long, can't find any reference to this solution online?
				long startTime = System.nanoTime();
				rawImage = LiveView.this.device.getScreenshot(); // currently this takes ~10 seconds, WHY?!?!?!!
				long endTime = System.nanoTime();
				long duration1 = ((endTime - startTime)/1000000);
				System.out.println("(1) Got a screenshot! Only took: " + duration1 + " milliseconds " + (duration1 < 3000 ? ":)" : ":("));
				
				// Method #2: taking screenshot using the adb shell in terminal takes ~2s
				// Doesn't work: don't know where it would save the image
//				startTime = System.nanoTime();
				long time = System.currentTimeMillis();
				String home = System.getProperty("user.home");
				// TODO: remember to wait ~2 seconds before using image
//				Runtime.getRuntime().exec(home + "/android-sdks/platform-tools/adb shell screencap -p | perl -pe 's/\\x0D\\x0A/\\x0A/g' > " + home + "/Desktop/screen" + time + ".png");
//				endTime = System.nanoTime();
//				long duration2 = ((endTime - startTime)/1000000);
//				System.out.println("(2) Got another screenshot! Only took: " + duration2 + " milliseconds " + (duration2 < 3000 ? ":)" : ":("));
				
				// TODO: pull screencap from device (taking screenshot part works)
				// Method #3
				// Save screenshot onto device, load from device onto display's home, then load from local
				// currently commented out to avoid accidentally filling a phone with screenshots
				startTime = System.nanoTime();
//				Runtime.getRuntime().exec(home + "/android-sdks/platform-tools/adb shell /system/bin/screencap -p /sdcard/screenshot_" + time + ".png");
				// Must wait 2 seconds for screenshot to be created
				Thread.sleep(2000);
				System.out.println("screenshot named: screenshot_" + time + ".png");
//				Runtime.getRuntime().exec(home + "/android-sdks/platform-tools/adb shell pull /sdcard/screenshot_" + time + ".png " + home + "/screenshot_" + time + ".png");
				// TODO: deletes image from phone, but gallery shows "Missing Media" image...
//				Runtime.getRuntime().exec(home + "/android-sdks/platform-tools/adb shell rm /sdcard/screenshot_" + time + ".png");
				endTime = System.nanoTime();
				long duration3 = ((endTime - startTime)/1000000);
				System.out.println("(3) Got another screenshot! Only took: " + duration3 + " milliseconds " + (duration3 < 3000 ? ":)" : ":("));
				
			} catch (IOException ioe) {
				System.out.println("failed to get a screenshot");
				return Boolean.valueOf(false);
			}

			boolean resize = false;
			boolean landscape = false;
			try {
				if (rawImage != null) {
					resize = true;
					// rotate image dimensions if landscape is true
					int width2 = landscape ? rawImage.height : rawImage.width;
					int height2 = landscape ? rawImage.width : rawImage.height;
					LiveView.this.image = new BufferedImage(width2, height2, BufferedImage.TYPE_INT_RGB);
					image.getScaledInstance(width, height, Image.SCALE_SMOOTH );
					int index = 0;
					int indexInc = rawImage.bpp >> 3;
					for (int y = 0; y < rawImage.height; y++) {
						// paint the image row by row, works very fast
						for (int x = 0; x < rawImage.width; x++, index += indexInc) {
							int value = rawImage.getARGB(index);
							if (landscape)
								image.setRGB(y, rawImage.width - x - 1, value);
							else
								image.setRGB(x, y, value);
						}
					}
				}
			} finally {

			}

			return Boolean.valueOf(resize);
		}

		protected void done() {
			try {
				if (((Boolean)get()).booleanValue()) {
					LiveView.this.validate();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			LiveView.this.repaint();
		}
	}

	class ScreenshotViewer extends JComponent {

		ScreenshotViewer() {
			setOpaque(true);
			setBounds(0,0,width,height);

			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			// For highlighting the live view?
			JPanel glass = new JPanel();
			glass.setSize(new Dimension(width, height));
			glass.setPreferredSize(new Dimension(width, height));
			glass.setMaximumSize(new Dimension(width, height));
			glass.setOpaque(false);
			glass.setBounds(0,0,width,height);
			instructionSingleton.setHighlightContainer(glass);
			instructionSingleton.highlight("nothing", "camera");

			add(glass);
		}

		protected void paintComponent(Graphics g) {
			g.fillRect(0, 0, width, height);
			if (LiveView.this.isLoading) {
				return;
			}

			if (LiveView.this.image != null) {
				// if live view not null paint new image - this should be called every second
				g.drawImage(LiveView.this.image, 0, 0, width, height, null);
			}
		}

		public Dimension getPreferredSize() {
			return new Dimension(width, height);
		}
	}

	class Crosshair extends JPanel {
		private final LiveView.ScreenshotViewer screenshotViewer;

		Crosshair(LiveView.ScreenshotViewer screenshotViewer) {
			this.screenshotViewer = screenshotViewer;
			setOpaque(true);
			setLayout(new BorderLayout());
			setBounds(0,0,width, height);
			add(screenshotViewer);
		}

		public Dimension getPreferredSize() {
			return this.screenshotViewer.getPreferredSize();
		}

		public Dimension getMaximumSize() {
			return this.screenshotViewer.getPreferredSize();
		}
	}
}