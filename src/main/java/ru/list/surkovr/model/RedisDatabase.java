package ru.list.surkovr.model;

import com.google.inject.Inject;
import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;
import ru.list.surkovr.api.VisitedLinksRequest;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.System.out;

@Singleton
public class RedisDatabase implements AbstractDatabase {

    @Inject
    @Named("REDIS_ADDRESS")
    public String redisAddress;
    @Inject
    @Named("REDIS_SET_KEY")
    private String key;
    private RedissonClient redisson;
    private RKeys rKeys;
    private RScoredSortedSet<String> visitedLinksRedisSet;

    @Override
    @Inject
    public void init() {
        Config config = new Config();
        config.useSingleServer().setAddress(redisAddress);
        try {
            redisson = Redisson.create(config);
        } catch (RedisConnectionException Exc) {
            out.println(Exc.getMessage());
        }
        rKeys = redisson.getKeys();
        visitedLinksRedisSet = redisson.getScoredSortedSet(key);
    }

    @Override
    public void saveObject(VisitedLinksRequest vLinks) {
        double score = (double) getScore();
        vLinks.getLinks().forEach(l -> visitedLinksRedisSet.add(score, l)); // Overrides score if exists
    }

    @Override
    public List<String> getListOfLinks(long from, long to) {
        Collection<String> collection = visitedLinksRedisSet
                .valueRange(from, true, to, true);
        return new ArrayList<>(collection);
    }

    public Long getScore() {
        return System.currentTimeMillis();
    }

    @Override
    public void close() {
        redisson.shutdown();
    }
}
