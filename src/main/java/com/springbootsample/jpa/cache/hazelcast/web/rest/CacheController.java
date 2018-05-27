package com.springbootsample.jpa.cache.hazelcast.web.rest;

import com.hazelcast.map.impl.proxy.MapProxyImpl;
import com.hazelcast.monitor.LocalMapStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/cacheNames")
    public Collection<String> add() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        return cacheNames;
    }


    @GetMapping("/stats")
    public Map<String, LocalMapStats>  cachestats() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        Map<String, LocalMapStats> localMapStats = new HashMap<>();
        for (String cacheName : cacheNames) {
            Cache cache =cacheManager.getCache(cacheName);
            MapProxyImpl hazelcastCache= (MapProxyImpl) cache.getNativeCache();
            localMapStats.put(cacheName, hazelcastCache.getLocalMapStats());
        }
        return localMapStats;
    }

    @GetMapping("/clearAll")
    public Map<String, LocalMapStats>  clearAll() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        Map<String, LocalMapStats> localMapStats = new HashMap<>();
        for (String cacheName : cacheNames) {
            Cache cache =cacheManager.getCache(cacheName);
            cache.clear();
        }
        return localMapStats;
    }

    @GetMapping("/clearOne")
    public void clear(String cacheName) {
        Cache cache =cacheManager.getCache(cacheName);
        cache.clear();
    }
}
