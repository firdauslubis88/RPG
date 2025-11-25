package engine.video;

/**
 * Week 13-03: Tightly Coupled Subsystems (ANTI-PATTERN)
 *
 * Complex video/rendering subsystem that client must interact with directly.
 * In a real game engine, this would have:
 * - Window (OS window management)
 * - GLContext (OpenGL/DirectX context)
 * - ShaderManager (GLSL shaders)
 * - FrameBuffer (render targets)
 * - Renderer (actual rendering)
 *
 * Client must know the CORRECT ORDER to initialize all these!
 */
public class VideoSystem {
    private boolean windowCreated = false;
    private boolean contextReady = false;
    private boolean shadersLoaded = false;
    private boolean frameBufferReady = false;
    private int width = 0;
    private int height = 0;

    /**
     * Step 1: Create window
     * MUST be called first!
     */
    public void createWindow(int width, int height, String title) {
        System.out.println("[Window] Creating window...");
        System.out.println("  → Title: " + title);
        System.out.println("  → Size: " + width + "x" + height);
        this.width = width;
        this.height = height;
        windowCreated = true;
        System.out.println("  ✓ Window created");
    }

    /**
     * Step 2: Initialize graphics context
     * REQUIRES: createWindow() first!
     */
    public void initContext() {
        if (!windowCreated) {
            System.out.println("[GLContext] ✗ ERROR: Window not created!");
            System.out.println("  → Client forgot to call createWindow() first!");
            return;
        }
        System.out.println("[GLContext] Initializing OpenGL context...");
        System.out.println("  → OpenGL version: 4.5");
        contextReady = true;
        System.out.println("  ✓ Context initialized");
    }

    /**
     * Step 3: Load shaders
     * REQUIRES: initContext() first!
     */
    public void loadShaders(String vertexShader, String fragmentShader) {
        if (!contextReady) {
            System.out.println("[ShaderManager] ✗ ERROR: Context not ready!");
            System.out.println("  → Client forgot to call initContext() first!");
            return;
        }
        System.out.println("[ShaderManager] Loading shaders...");
        System.out.println("  → Vertex: " + vertexShader);
        System.out.println("  → Fragment: " + fragmentShader);
        shadersLoaded = true;
        System.out.println("  ✓ Shaders compiled and linked");
    }

    /**
     * Step 4: Create frame buffer
     * REQUIRES: initContext() first!
     */
    public void createFrameBuffer(int width, int height) {
        if (!contextReady) {
            System.out.println("[FrameBuffer] ✗ ERROR: Context not ready!");
            System.out.println("  → Client forgot to call initContext() first!");
            return;
        }
        System.out.println("[FrameBuffer] Creating frame buffer...");
        System.out.println("  → Size: " + width + "x" + height);
        frameBufferReady = true;
        System.out.println("  ✓ Frame buffer created");
    }

    /**
     * Begin rendering frame
     * REQUIRES: All initialization complete!
     */
    public void beginFrame() {
        if (!shadersLoaded || !frameBufferReady) {
            System.out.println("[Renderer] ✗ ERROR: Not fully initialized!");
            return;
        }
        System.out.println("[Renderer] Begin frame...");
    }

    /**
     * Render the scene
     */
    public void render() {
        System.out.println("[Renderer] Rendering scene (" + width + "x" + height + ")");
    }

    /**
     * End frame and swap buffers
     */
    public void endFrame() {
        System.out.println("[Renderer] End frame, swap buffers");
    }

    /**
     * Cleanup - client must call this in CORRECT ORDER!
     * Frame buffer → Shaders → Context → Window
     */
    public void cleanup() {
        System.out.println("[VideoSystem] Cleaning up...");
        if (frameBufferReady) {
            System.out.println("  → Destroying frame buffer");
            frameBufferReady = false;
        }
        if (shadersLoaded) {
            System.out.println("  → Releasing shaders");
            shadersLoaded = false;
        }
        if (contextReady) {
            System.out.println("  → Destroying GL context");
            contextReady = false;
        }
        if (windowCreated) {
            System.out.println("  → Closing window");
            windowCreated = false;
        }
        System.out.println("  ✓ Video system cleaned up");
    }
}
