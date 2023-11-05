package net.coderbot.batchedentityrendering.impl;

import com.gtnewhorizons.angelica.compat.mojang.BufferBuilder;
import com.gtnewhorizons.angelica.compat.mojang.DrawState;
import com.gtnewhorizons.angelica.compat.mojang.MultiBufferSource;
import com.gtnewhorizons.angelica.compat.mojang.RenderLayer;
import com.gtnewhorizons.angelica.compat.mojang.VertexConsumer;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SegmentedBufferBuilder implements MultiBufferSource, MemoryTrackingBuffer {
    private final BufferBuilder buffer;
    private final List<RenderLayer> usedTypes;
    private RenderLayer currentType;

    public SegmentedBufferBuilder() {
        // 2 MB initial allocation
        this.buffer = new BufferBuilder(512 * 1024);
        this.usedTypes = new ArrayList<>(256);

        this.currentType = null;
    }

    @Override
    public VertexConsumer getBuffer(RenderLayer renderType) {
        if (!Objects.equals(currentType, renderType)) {
            if (currentType != null) {
                if (shouldSortOnUpload(currentType)) {
                    buffer.sortQuads(0, 0, 0);
                }

                buffer.end();
                usedTypes.add(currentType);
            }

            buffer.begin(renderType.mode(), renderType.format());

            currentType = renderType;
        }

        // Use duplicate vertices to break up triangle strips
        // https://developer.apple.com/library/archive/documentation/3DDrawing/Conceptual/OpenGLES_ProgrammingGuide/Art/degenerate_triangle_strip_2x.png
        // This works by generating zero-area triangles that don't end up getting rendered.
        // TODO: How do we handle DEBUG_LINE_STRIP?
        if (RenderTypeUtil.isTriangleStripDrawMode(currentType)) {
            ((BufferBuilderExt) buffer).splitStrip();
        }

        return buffer;
    }

    public List<BufferSegment> getSegments() {
        if (currentType == null) {
            return Collections.emptyList();
        }

        usedTypes.add(currentType);

        if (shouldSortOnUpload(currentType)) {
            buffer.sortQuads(0, 0, 0);
        }

        buffer.end();
        currentType = null;

        List<BufferSegment> segments = new ArrayList<>(usedTypes.size());

        for (RenderLayer type : usedTypes) {
            Pair<DrawState, ByteBuffer> pair = buffer.popNextBuffer();

            DrawState drawState = pair.getLeft();
            ByteBuffer slice = pair.getRight();

            segments.add(new BufferSegment(slice, drawState, type));
        }

        usedTypes.clear();

        return segments;
    }

    private static boolean shouldSortOnUpload(RenderLayer type) {
        return type.shouldSortOnUpload();
    }

    @Override
    public int getAllocatedSize() {
        return ((MemoryTrackingBuffer) buffer).getAllocatedSize();
    }

    @Override
    public int getUsedSize() {
        return ((MemoryTrackingBuffer) buffer).getUsedSize();
    }
}
