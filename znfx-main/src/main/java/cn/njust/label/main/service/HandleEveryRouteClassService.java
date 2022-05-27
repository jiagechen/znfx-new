package cn.njust.label.main.service;

import cn.njust.label.main.service.impl.HandleEveryRouteClassServiceImpl;

public interface HandleEveryRouteClassService {
    void handleEveryRouteClass();

    public static void main(String[] args) {
        HandleEveryRouteClassService handleEveryRouteClassService = new HandleEveryRouteClassServiceImpl();
        handleEveryRouteClassService.handleEveryRouteClass();
    }
}
