/*
 * Copyright (C) 2013-2017 microG Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.appdatasearch;

import android.os.Bundle;

import org.microg.safeparcel.AutoSafeParcelable;
import org.microg.safeparcel.SafeParceled;

public class CorpusStatus extends AutoSafeParcelable {

    @SafeParceled(1000)
    private int versionCode;
    @SafeParceled(1)
    public boolean found;
    @SafeParceled(2)
    public long lastIndexedSeqno;
    @SafeParceled(3)
    public long lastCommittedSeqno;
    @SafeParceled(4)
    public long committedNumDocuments;
    @SafeParceled(5)
    public Bundle counters;
    @SafeParceled(6)
    public String g;

    public CorpusStatus() {
        versionCode = 2;
    }

    public static final Creator<CorpusStatus> CREATOR = new AutoCreator<CorpusStatus>(CorpusStatus.class);
}
