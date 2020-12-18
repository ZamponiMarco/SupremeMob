package com.github.jummes.suprememob.api;

import com.github.jummes.supremeitem.database.NamedModel;
import com.github.jummes.suprememob.SupremeMob;
import com.github.jummes.suprememob.mob.Mob;

import java.util.List;
import java.util.stream.Collectors;

public class SupremeMobAPI {

    public List<String> getMobsNames() {
        return SupremeMob.getInstance().getMobManager().getMobs().stream().map(NamedModel::getName).
                collect(Collectors.toList());
    }

    public Mob getByName(String name) {
        return SupremeMob.getInstance().getMobManager().getByName(name);
    }

}
