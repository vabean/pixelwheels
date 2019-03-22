/*
 * Copyright 2018 Aurélien Gâteau <mail@agateau.com>
 *
 * This file is part of Pixel Wheels.
 *
 * Pixel Wheels is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.agateau.pixelwheels.stats;

import com.agateau.pixelwheels.map.Championship;
import com.agateau.utils.CollectionUtils;

import java.util.HashMap;

public class GameStatsImpl implements GameStats {
    private final transient IO mIO;
    private transient Listener mListener;
    final HashMap<String, TrackStats> mTrackStats = new HashMap<String, TrackStats>();
    final HashMap<String, Integer> mBestChampionshipRank = new HashMap<String, Integer>();
    final HashMap<String, Integer> mEvents = new HashMap<String, Integer>();

    public interface IO {
        void setGameStats(GameStatsImpl gameStats);
        void load();
        void save();
    }

    public GameStatsImpl(IO io) {
        mIO = io;
        mIO.setGameStats(this);
        mIO.load();
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public TrackStats getTrackStats(String trackId) {
        TrackStats stats = mTrackStats.get(trackId);
        if (stats == null) {
            stats = addTrack(trackId);
        }
        return stats;
    }

    @Override
    public TrackStats addTrack(String trackId) {
        TrackStats stats = new TrackStats(this);
        mTrackStats.put(trackId, stats);
        return stats;
    }

    @Override
    public int getBestChampionshipRank(Championship championship) {
        //noinspection ConstantConditions
        return CollectionUtils.getOrDefault(mBestChampionshipRank, championship.getId(), Integer.MAX_VALUE);
    }

    @Override
    public void onChampionshipFinished(Championship championship, int rank) {
        Integer currentBest = mBestChampionshipRank.get(championship.getId());
        if (currentBest == null || currentBest > rank) {
            mBestChampionshipRank.put(championship.getId(), rank);
            save();
        }
    }

    @Override
    public void recordEvent(Event event) {
        String id = event.toString();
        Integer count = mEvents.get(id);
        if (count == null) {
            count = 0;
        }
        ++count;
        mEvents.put(id, count);
        save();
    }

    @Override
    public int getEventCount(Event event) {
        //noinspection ConstantConditions
        return CollectionUtils.getOrDefault(mEvents, event.toString(), 0);
    }

    public void save() {
        if (mListener != null) {
            mListener.onChanged();
        }
        mIO.save();
    }
}
