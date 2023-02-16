package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.music.Orchestra;


public class Music extends SubsystemBase {
    private static Music mMusic;
    private static Orchestra mOrchestra;

    private static Boolean Status;
    private static String Song;
    private static int SongNum = -1;

    private static String[] playlist = {
        "Crab-Rave.chrp",
        "file1.chrp",
        "file2.chrp"
    };

    public static int playlistLength = playlist.length;


    public static Music getInstance() {
        if(mMusic == null) {
            mMusic = new Music();
        }
        return mMusic;
    }

    public Music() {
        restartPlaylist();
    }

    public static void defaultCode() {
        SmartDashboard.putNumber("TimeStamp", mOrchestra.getCurrentTime());
        SmartDashboard.putString("Song", Song);
        playerStatus();
    }

    public static void playSong() {
        mOrchestra.play();
    }

    public static void loadSong(String filename) {
        mOrchestra.loadMusic(filename);
        Song = filename;
    }

    public static void skipSong() {
        loadSong(playlistOrder());
    }

    public static void pauseSong() {
        mOrchestra.pause();
    }

    public static void playerStatus() {
        Status = mOrchestra.isPlaying();
        SmartDashboard.putBoolean("Song Playing", Status);
    }

    public static void addInstrument(TalonFX input) {
        mOrchestra.addInstrument(input);
    }

    public static String playlistOrder() { 
        if (SongNum == (playlistLength - 1)) {
            SongNum = 0;
        } else {
            SongNum++;
        }
        return playlist[SongNum];
    }

    public static void restartPlaylist() {
        SongNum = (playlistLength - 1);
        loadSong(playlistOrder());
    }
}
