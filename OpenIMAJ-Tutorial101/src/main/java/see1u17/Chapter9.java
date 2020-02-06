package see1u17;

import java.net.MalformedURLException;
import java.net.URL;

import org.openimaj.audio.SampleChunk;
import org.openimaj.video.xuggle.XuggleAudio;
import org.openimaj.vis.audio.AudioWaveform;

public class Chapter9 {
	public static void main(String[] args) throws Exception{
		audioProcessing();
	}
	
	private static void audioProcessing() throws MalformedURLException {
		final AudioWaveform vis = new AudioWaveform( 400, 400 );
		vis.showWindow( "Waveform" );

		final XuggleAudio xa = new XuggleAudio( 
		    new URL( "http://www.audiocheck.net/download.php?" +
		        "filename=Audio/audiocheck.net_sweep20-20klin.wav" ) );

		SampleChunk sc = null;
		while( (sc = xa.nextSampleChunk()) != null )
		    vis.setData( sc.getSampleBuffer() );
	}
}
