package shagejack.lostgrace.foundation.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import shagejack.lostgrace.foundation.utility.Color;
import shagejack.lostgrace.foundation.utility.Vector3;

import java.util.List;

public class DrawUtils {

    private DrawUtils() {
        throw new IllegalStateException(this.getClass().toString() + "should not be instantiated as it's a utility class.");
    }

    public static final List<TriangleFace> sphereFaces = new SphereBuilder().build(4, 16, true);

    public static void renderSimpleIcon(VertexConsumer builder, PoseStack renderStack, TextureAtlasSprite sprite, Color color, int alpha, float scale) {
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        Matrix4f mat = renderStack.last().pose();
        builder.vertex(mat, scale * -0.5F, scale * -0.5F, 0.0F).color(r, g, b, alpha).uv(u0, v1).uv2(LightTexture.FULL_BRIGHT).normal(0, 0, -1).endVertex();
        builder.vertex(mat, scale * -0.5F, scale * 0.5F, 0.0F).color(r, g, b, alpha).uv(u0, v0).uv2(LightTexture.FULL_BRIGHT).normal(0, 0, -1).endVertex();
        builder.vertex(mat, scale * 0.5F, scale * 0.5F, 0.0F).color(r, g, b, alpha).uv(u1, v0).uv2(LightTexture.FULL_BRIGHT).normal(0, 0, -1).endVertex();
        builder.vertex(mat, scale * 0.5F, scale * -0.5F, 0.0F).color(r, g, b, alpha).uv(u1, v1).uv2(LightTexture.FULL_BRIGHT).normal(0, 0, -1).endVertex();
    }

    // Too many method overloads...
    public static void renderQuad(VertexConsumer builder, PoseStack renderStack, Vector3 pos, Vector3 facingNormal, float scale, TextureAtlasSprite sprite) {
        renderQuad(builder, renderStack, pos, facingNormal, 0, scale, sprite, 255);
    }

    public static void renderQuad(VertexConsumer builder, PoseStack renderStack, Vector3 pos, Vector3 facingNormal, float scale, TextureAtlasSprite sprite, int alpha) {
        renderQuad(builder, renderStack, pos, facingNormal, 0, scale, sprite, LightTexture.FULL_BRIGHT, 255, 255, 255, alpha);
    }

    public static void renderQuad(VertexConsumer builder, PoseStack renderStack, Vector3 pos, Vector3 facingNormal, double planeRotationAngle, float scale, TextureAtlasSprite sprite) {
        renderQuad(builder, renderStack, pos, facingNormal, planeRotationAngle, scale, sprite, 255);
    }

    public static void renderQuad(VertexConsumer builder, PoseStack renderStack, Vector3 pos, Vector3 facingNormal, double planeRotationAngle, float scale, TextureAtlasSprite sprite, int alpha) {
        renderQuad(builder, renderStack, pos, facingNormal, planeRotationAngle, scale, sprite, LightTexture.FULL_BRIGHT, 255, 255, 255, alpha);
    }

    public static void renderQuad(VertexConsumer builder, PoseStack renderStack, Vector3 pos, Vector3 facingNormal, double planeRotationAngle, float scale, TextureAtlasSprite sprite, int lightMapUV, int r, int g, int b, int alpha) {
        renderQuad(builder, renderStack, pos, facingNormal, planeRotationAngle, scale, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), lightMapUV, r, g, b, alpha);
    }

    public static void renderQuad(VertexConsumer builder, PoseStack renderStack, Vector3 pos, Vector3 facingNormal, double planeRotationAngle, float scale, float u0, float v0, float u1, float v1, int alpha) {
        renderQuad(builder, renderStack, pos, facingNormal, planeRotationAngle, scale, u0, v0, u1, v1, LightTexture.FULL_BRIGHT, 255, 255, 255, alpha);
    }

    // FIXME: 2022/5/19 fix the problem that quads with some certain facings not rendered as expected (incorrect and unforeseen plane rotation which may caused by wrong implementation of Vector3#asToVecRotation)
    public static void renderQuad(VertexConsumer builder, PoseStack renderStack, Vector3 pos, Vector3 facingNormal, double planeRotationAngle, float scale, float u0, float v0, float u1, float v1, int lightMapUV, int r, int g, int b, int alpha) {

        Vector3 pos1, pos2, pos3, pos4;

        // in case that input facing normal is not normalized
        Vector3 normal = facingNormal.normalize();
        Vector3 planeRotationAxis = Vector3.Z_NEG_AXIS;

        if (!facingNormal.isParallelTo(Vector3.Z_NEG_AXIS)) {
            Quaternion rotation = Vector3.Z_NEG_AXIS.asToVecRotation(normal);

            pos1 = pos.add(new Vector3(-scale, -scale, 0).rotateDegree(planeRotationAngle, planeRotationAxis).transform(rotation));
            pos2 = pos.add(new Vector3(-scale, scale, 0).rotateDegree(planeRotationAngle, planeRotationAxis).transform(rotation));
            pos3 = pos.add(new Vector3(scale, scale, 0).rotateDegree(planeRotationAngle, planeRotationAxis).transform(rotation));
            pos4 = pos.add(new Vector3(scale, -scale, 0).rotateDegree(planeRotationAngle, planeRotationAxis).transform(rotation));
        } else {
            pos1 = pos.add(-scale, -scale, 0).rotateDegree(planeRotationAngle, planeRotationAxis);
            pos2 = pos.add(-scale, scale, 0).rotateDegree(planeRotationAngle, planeRotationAxis);
            pos3 = pos.add(scale, scale, 0).rotateDegree(planeRotationAngle, planeRotationAxis);
            pos4 = pos.add(scale, -scale, 0).rotateDegree(planeRotationAngle, planeRotationAxis);
        }

        Matrix4f renderMatrix = renderStack.last().pose();

        pos1.drawPosVertex(renderMatrix, builder).color(r, g, b, alpha).uv(u0, v1).uv2(lightMapUV).normal(normal.xF(), normal.yF(), normal.zF()).endVertex();
        pos2.drawPosVertex(renderMatrix, builder).color(r, g, b, alpha).uv(u0, v0).uv2(lightMapUV).normal(normal.xF(), normal.yF(), normal.zF()).endVertex();
        pos3.drawPosVertex(renderMatrix, builder).color(r, g, b, alpha).uv(u1, v0).uv2(lightMapUV).normal(normal.xF(), normal.yF(), normal.zF()).endVertex();
        pos4.drawPosVertex(renderMatrix, builder).color(r, g, b, alpha).uv(u1, v1).uv2(lightMapUV).normal(normal.xF(), normal.yF(), normal.zF()).endVertex();
    }

    public static void renderTriangleWithColor(VertexConsumer builder, PoseStack renderStack, Vector3 pos, TriangleFace triangle, Color color, int alpha) {
        renderTriangleWithColor(builder, renderStack, pos, triangle, LightTexture.FULL_BRIGHT, color, alpha);
    }

    public static void renderTriangleWithColor(VertexConsumer builder, PoseStack renderStack, Vector3 pos, TriangleFace triangle, int lightMapUV, Color color, int alpha) {
        Matrix4f renderMatrix = renderStack.last().pose();

        int r, g, b;
        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();

        pos.add(triangle.getV1()).drawPosVertex(renderMatrix, builder).color(r, g, b, alpha).uv2(lightMapUV).endVertex();
        pos.add(triangle.getV2()).drawPosVertex(renderMatrix, builder).color(r, g, b, alpha).uv2(lightMapUV).endVertex();
        pos.add(triangle.getV3()).drawPosVertex(renderMatrix, builder).color(r, g, b, alpha).uv2(lightMapUV).endVertex();
    }

    public static void renderTriangleWithColor(VertexConsumer builder, PoseStack renderStack, TriangleFace triangle, int lightMapUV, Color color, int alpha) {
        Matrix4f renderMatrix = renderStack.last().pose();

        int r, g, b;
        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();

        triangle.getV1().drawPosVertex(renderMatrix, builder).color(r, g, b, alpha).uv2(lightMapUV).endVertex();
        triangle.getV2().drawPosVertex(renderMatrix, builder).color(r, g, b, alpha).uv2(lightMapUV).endVertex();
        triangle.getV3().drawPosVertex(renderMatrix, builder).color(r, g, b, alpha).uv2(lightMapUV).endVertex();
    }

    public static void renderTriangleWithTexture(VertexConsumer builder, PoseStack renderStack, Vector3 pos, TriangleFace triangle, TextureAtlasSprite textureAtlasSprite, int lightMapUV, Color color, int alpha) {
        renderTriangleWithTexture(builder, renderStack, pos, triangle, textureAtlasSprite.getU0(), textureAtlasSprite.getV0(), textureAtlasSprite.getU1(), textureAtlasSprite.getV1(), lightMapUV, color, alpha);
    }

    public static void renderTriangleWithTexture(VertexConsumer builder, PoseStack renderStack, Vector3 pos, TriangleFace triangle, float u0, float v0, float u1, float v1, int lightMapUV, Color color, int alpha) {
        Matrix4f renderMatrix = renderStack.last().pose();

        int r, g, b;
        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();

        pos.add(triangle.getV1()).drawPosVertex(renderMatrix, builder).uv(u0, v0).uv2(lightMapUV).color(r, g, b, alpha).endVertex();
        pos.add(triangle.getV2()).drawPosVertex(renderMatrix, builder).uv(u1, v0).uv2(lightMapUV).color(r, g, b, alpha).endVertex();
        pos.add(triangle.getV3()).drawPosVertex(renderMatrix, builder).uv(u0, v1).uv2(lightMapUV).color(r, g, b, alpha).endVertex();
    }

    public static void renderSphere(PoseStack renderStack, Vector3 pos, double radius, Color color, int alpha) {
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer builder = buffer.getBuffer(RenderTypeLG.SPHERE);

        renderStack.pushPose();

        renderStack.scale((float) radius / 4, (float) radius / 4, (float) radius / 4);

        for (TriangleFace face : sphereFaces) {
            renderTriangleWithColor(builder, renderStack, pos, face, LightTexture.FULL_BRIGHT, color, alpha);
        }

        renderStack.popPose();

        buffer.endBatch(RenderTypeLG.SPHERE);
    }

    public static void renderSphere(PoseStack renderStack, double radius, Color color, int alpha) {
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer builder = buffer.getBuffer(RenderTypeLG.SPHERE);

        renderStack.pushPose();

        renderStack.scale((float) radius / 4, (float) radius / 4, (float) radius / 4);

        for (TriangleFace face : sphereFaces) {
            renderTriangleWithColor(builder, renderStack, face, LightTexture.FULL_BRIGHT, color, alpha);
        }

        renderStack.popPose();

        buffer.endBatch(RenderTypeLG.SPHERE);
    }

    public static void renderSphereWithTexture(PoseStack renderStack, double radius, TextureAtlasSprite textureAtlasSprite, Color color, int alpha) {
        renderSphereWithTexture(renderStack, Vector3.ZERO, radius, textureAtlasSprite, LightTexture.FULL_BRIGHT, color, alpha);
    }

    public static void renderSphereWithTexture(PoseStack renderStack, Vector3 pos, double radius, TextureAtlasSprite textureAtlasSprite, int lightMapUV, Color color, int alpha) {
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer builder = buffer.getBuffer(RenderTypeLG.SPHERE_TEX);

        renderStack.pushPose();

        renderStack.scale((float) radius / 4, (float) radius / 4, (float) radius / 4);

        for (TriangleFace face : sphereFaces) {
            renderTriangleWithTexture(builder, renderStack, pos, face, textureAtlasSprite, lightMapUV, color, alpha);
        }

        renderStack.popPose();

        buffer.endBatch(RenderTypeLG.SPHERE_TEX);
    }

    public static int renderInLevelText(PoseStack renderStack, Vector3 pos, Component text, Color color, float scale) {
        return renderInLevelText(renderStack, pos, text, color, scale, true);
    }

    public static int renderInLevelText(PoseStack renderStack, Vector3 pos, Component text, Color color, float scale, boolean alwaysFacingPlayer) {
        Font font = Minecraft.getInstance().font;

        renderStack.pushPose();
        renderStack.translate(pos.x(), pos.y(), pos.z());
        renderStack.scale(-0.05f * scale, -0.05f * scale, -0.05f * scale);

        if (alwaysFacingPlayer) {
            Quaternion rotation = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
            renderStack.mulPose(rotation);
        }

        int drawLength = font.drawShadow(renderStack, text, -font.width(text) / 2.0f, 0, color.getRGB());

        renderStack.popPose();

        return drawLength;
    }

    public static int renderInLevelText(PoseStack renderStack, Component text, Color color, float scale, boolean alwaysFacingPlayer) {
        Font font = Minecraft.getInstance().font;

        renderStack.pushPose();
        renderStack.scale(-0.05f * scale, -0.05f * scale, -0.05f * scale);

        if (alwaysFacingPlayer) {
            Quaternion rotation = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
            renderStack.mulPose(rotation);
        }

        int drawLength = font.drawShadow(renderStack, text, -font.width(text) / 2.0f, 0, color.getRGB());

        renderStack.popPose();

        return drawLength;
    }

}
