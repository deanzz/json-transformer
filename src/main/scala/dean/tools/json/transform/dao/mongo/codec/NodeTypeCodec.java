package dean.tools.json.transform.dao.mongo.codec;

import dean.tools.json.transform.enums.NodeType;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;


public class NodeTypeCodec implements Codec<NodeType> {
    @Override
    public void encode(final BsonWriter writer, final NodeType value, final EncoderContext encoderContext) {
        writer.writeString(value.name());
    }

    @Override
    public NodeType decode(final BsonReader reader, final DecoderContext decoderContext) {
        return NodeType.valueOf(reader.readString());
    }

    @Override
    public Class<NodeType> getEncoderClass() {
        return NodeType.class;
    }
}