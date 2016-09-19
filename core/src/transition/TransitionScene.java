package transition;

/**
 * Created by ivan-asen on 18.09.16.
 */
public class TransitionScene {
//    private boolean complete;
//
//    private float inX;
//    private float inY;
//    private float outX;
//    private float outY;
//
//    private Scene inScene;
//    private Scene outScene;
//    private Group inSceneRoot;
//    private Group outSceneRoot;
//
//    private int durationMillis;
//    private TweenEquation easeEquation;
//
//    /**
//     * Enter handler makes a note of scene contents position.
//     *
//     */
//    @Override
//    public void enter()
//    {
//        this.complete = false;
//
//        inX = inSceneRoot.x;
//        inY = inSceneRoot.y;
//
//        outX = outSceneRoot.x;
//        outY = outSceneRoot.y;
//    }
//
//    /**
//     * Exit handler resets scene contents positions.
//     *
//     */
//    @Override
//    public void exit()
//    {
//        this.complete = true;
//
//        inSceneRoot.x = inX;
//        inSceneRoot.y = inY;
//
//        outSceneRoot.x = outX;
//        outSceneRoot.y = outY;
//    }
//
//    /**
//     * Draw both scenes as we animated contents.
//     *
//     */
//    @Override
//    public void draw()
//    {
//        // Move
//        inSceneRoot.act(Gdx.graphics.getDeltaTime());
//        outSceneRoot.act(Gdx.graphics.getDeltaTime());
//
//        // Draw
//        if (!complete)
//        {
//            outScene.draw();
//        }
//        inScene.draw();
//    }
//
//    /**
//     * Default transition handlers
//     */
//    @Override
//    public void onEvent(EventType eventType, BaseTween source)
//    {
//        switch (eventType)
//        {
//            case COMPLETE:
//                Director.instance().setScene(this.inScene);
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * Transition complete.
//     *
//     * @return The transition complete handler.
//     */
//    public boolean isComplete()
//    {
//        return complete;
//    }
}
