/*
 * Copyright 2022 Infineon Technologies AG.
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
 *
 */
package com.infineon.nfcbpsk.ui.adapters;

/**
 * Model for the links in the about screen
 */
public class LinkModel {
    /**
     * Name of the link
     */
    public final String name;
    /**
     * URL link
     */
    public final String link;
    /**
     * Type of view to be displayed on screen
     */
    public final int type;
    /**
     * Method to open the link: WEBVIEW or BROWSER
     */
    public final String open;

    /**
     * Constructor to generate a link model
     *
     * @param name  Link name
     * @param link  Link URL
     * @param type  Type of view to be display on screen
     * @param open  Method to open link: WEBVIEW or BROWSER
     */
    public LinkModel(String name, String link, int type, String open) {
        this.name = name;
        this.link = link;
        this.type = type;
        this.open = open;
    }
}
