package com.realestate.crawler.admin.commandside.repository;

import com.realestate.crawler.proto.CreateStaterUrl;
import com.realestate.crawler.proto.GetStaterUrls;
import com.realestate.crawler.proto.Starterurl;

import java.util.List;

public interface IStarterUrlRepository {

    boolean create(CreateStaterUrl createStaterUrl);

    List<Starterurl> getStarterUrls(GetStaterUrls getStaterUrls);
}
