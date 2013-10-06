package com.codeindustria.nim;

import java.util.Random;

public class NimClass {
	public static int[] _board;
	public static int no_heaps = 4;
	public static int gameType = 0; // 0 - misere | 1 - normal
	public static int playWith = 0; // 0 - master | 1 - friend
	
	public NimClass() {
		_board = new int[no_heaps];
		_board[0] = 1;
		_board[1] = 3;
		_board[2] = 5;
		_board[3] = 7;
	}
	
	public static int[] computerMove() {
        int heap;
        int no_sticks;
        int nim_sum;
        int heap_mod; // heap after modification
        int original_sticks = 0; // initial number of sticks
        int original_heap = 0; // inital heap who is to be modified
        int twoOrMore; // number of heaps with two or more sticks
        int no_ones; // number of sticks with 1 stick
        int no_zeros; // number of empty heaps;
        int i;
        int ans[] = {-1, -1};
        Random rand = new Random();
        int ok = 0;
        
        nim_sum = 0;
        for (i = 0; i < _board.length; i++) {
            nim_sum ^= _board[i];
            if (_board[i] != 0) ok = -1;
        }
        
        // Precaution
        if (ok == 0) return ans;
        
        if (nim_sum == 0) { // Computer doesn't stand a chance
            // Get random heap
            heap = rand.nextInt(no_heaps) + 1;
            while (true) {
                if (_board[heap-1] == 0) {
                    heap = (heap % no_heaps) + 1;
                } 
                else {
                    break;
                }
            }
            // Get random number of sticks
            no_sticks = rand.nextInt(_board[heap-1]) + 1;
            
            _board[heap-1] -= no_sticks;
            ans[0] = heap;
            ans[1] = no_sticks;
            return ans;
        } 
        else { // Computer will win
            if (gameType == 1) { // "Normal" play
                for (i = 0; i < no_heaps; i++) {
                    heap_mod = _board[i] ^ nim_sum;
                    if (heap_mod < _board[i]) {
                    	ans[0] = i+1;
                        ans[1] = _board[i] - heap_mod;
                        _board[i] = heap_mod;
                        return ans;
                    }
                }
            }
            else { // "Misere" play
                twoOrMore = 0;
                no_ones = 0;
                no_zeros = 0;
                for (i = 0; i < no_heaps; i++) {
                    if (_board[i] > 1 ) {
                        twoOrMore++;
                        // when these will be used, there will be only 1 heap with more than 1 stick
                        original_heap = i;
                        original_sticks = _board[i];
                    }
                    if (_board[i] == 1) {
                        no_ones++;
                    }
                    if (_board[i] == 0) {
                        no_zeros++;
                    }
                }
                // special case when there is only 1 heap left
                if (no_zeros == no_heaps - 1) {
                	ans[0] = original_heap + 1;
                    ans[1] = _board[original_heap]-1;
                    _board[original_heap] = 1;
                    return ans;
                }
                // there are more than 1 heap with two or more sticks left
                // play like "Take last" game
                if (twoOrMore != 1) { 
                    for (i = 0; i < no_heaps; i++) {
                        heap_mod = _board[i] ^ nim_sum;
                        if (heap_mod < _board[i]) {
                        	ans[0] = i + 1;
                            ans[1] = _board[i] - heap_mod;
                            _board[i] = heap_mod;
                            return ans;
                        }
                    }
                }
                // there is only 1 heap with two or more sticks left
                else {
                    if (no_ones % 2 == 1) {
                        _board[original_heap] = 0;
                        ans[0] = original_heap + 1;
                        ans[1] = original_sticks;
                        return ans;
                    }
                    else {
                        _board[original_heap] = 1;
                        ans[0] = original_heap + 1;
                        ans[1] = original_sticks - 1;
                        return ans;
                    }
                }
            }
        }
		return ans;
    }
	
	public static int lookForWinner () {
        int i;
        
        for (i = 0; i < no_heaps; i++) {
            if (_board[i] != 0) return -1;
        }
        return 0;
    }

}
