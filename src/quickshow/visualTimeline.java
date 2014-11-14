package quickshow;

public class visualTimeline {
	private final int timeLineWidth = 840;
	private final int timeLineHeight = 65;
	
	public visualTimeline(){
		
	}
	
	public void drawBackgroundCanvas(Quickshow q){
		q.rect(30, 525, timeLineWidth, timeLineHeight);
	}
}
