package com.github.mtruitt1;

import org.javacord.api.entity.channel.TextChannel;

import java.util.ArrayList;
import java.util.Collections;

public class RollResponse {
    public int rollTotal = 0;
    public String fullResponse;
    public ArrayList<String> rollClusters;
    public String comment;
    public TextChannel channel;

    public RollResponse(String user_mention, String dice_input, String c, TextChannel ch) {
        rollClusters = new ArrayList<>();
        String[] split_input = dice_input.trim().split("\\s+");
        for (String segment : split_input) {
            if (segment.contains("*")) {
                String[] multiply = segment.split("\\*");
                if (multiply.length > 2) {
                    rollClusters.add("X");
                } else {
                    try {
                        int times = Integer.parseInt(multiply[0]);
                        for (int i = 0; i < times; i++) {
                            rollClusters.add(multiply[1]);
                        }
                    } catch (Exception e) {
                        rollClusters.add("X");
                    }
                }
            } else {
                rollClusters.add(segment);
            }
        }
        comment = c;
        channel = ch;
        System.out.println(rollClusters.toString() + " " + comment);
        PerformRoll(user_mention);
    }

    public void PerformRoll(String mention) {
        rollTotal = 0;
        fullResponse = mention + ": " + (comment.length() > 0 ? "\"" + comment + "\" | Total = **" : "Total = **");
        String post_total = "";
        for (String cluster : rollClusters) {
            String line = "";
            try {
                int subTotal = 0;
                if (rollClusters.size() > 1) {
                    line += "\n> ";
                }
                String[] c_split = cluster.split("d");
                String dice_num = c_split[0];
                int highest = -1;
                int lowest = -1;
                int count = 0;
                if (dice_num.contains("l")) {
                    String[] n_split = dice_num.split("l");
                    count = Integer.parseInt(n_split[1]);
                    lowest = Integer.parseInt(n_split[0]);
                } else if (dice_num.contains("h")) {
                    String[] n_split = dice_num.split("h");
                    count = Integer.parseInt(n_split[1]);
                    highest = Integer.parseInt(n_split[0]);
                } else {
                    if (dice_num.length() > 0) {
                        count = Integer.parseInt(dice_num);
                    } else {
                        count = 1;
                    }
                    lowest = count;
                }
                String left = c_split[1];
                int extra = 0;
                int size = 0;
                if (left.contains("+")) {
                    String[] l_split = left.split("\\+");
                    size = Integer.parseInt(l_split[0]);
                    extra = Integer.parseInt(l_split[1]);
                } else {
                    size = Integer.parseInt(left);
                }
                ArrayList<Integer> raws = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    raws.add(Main.rand.nextInt(size) + 1);
                }
                Collections.sort(raws);
                int threshold = lowest;
                if (lowest == -1) {
                    Collections.reverse(raws);
                    threshold = highest;
                }
                String breakdown = " = [";
                String first = "";
                for (int i = 0; i < raws.size(); i++) {
                    if (i < threshold) {
                        subTotal += raws.get(i);
                        breakdown += first + raws.get(i).toString();
                    } else {
                        breakdown += first + "~~" + raws.get(i).toString() + "~~";
                    }
                    first = ", ";
                }
                breakdown += "]";
                if (extra != 0) {
                    subTotal += extra;
                    if (extra > 0) {
                        breakdown += " + " + extra;
                    } else {
                        breakdown += " - " + Math.abs(extra);
                    }
                }
                if (count == 1 && extra == 0) {
                    breakdown = "";
                }
                if (rollClusters.size() == 1) {
                    line += breakdown;
                } else {
                    line += count + "d" + size + ": *" + subTotal + "*" + breakdown;
                }
                if (cluster.contains("l")) {
                    line += ", lowest " + threshold + " of " + count;
                } else if (cluster.contains("h")) {
                    line += ", highest " + threshold + " of " + count;
                }
                rollTotal += subTotal;
            } catch (Exception e) {
                line = "*(Input skipped due to bad syntax.)*";
            }
            post_total += line;
        }
        fullResponse += rollTotal + "**" + post_total;
    }
}
