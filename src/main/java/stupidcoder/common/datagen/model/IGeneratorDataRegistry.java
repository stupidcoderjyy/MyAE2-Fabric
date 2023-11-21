package stupidcoder.common.datagen.model;

/**
 * 向生成器中注册数据
 */
@FunctionalInterface
public interface IGeneratorDataRegistry {
    void register();
}
