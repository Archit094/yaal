package on2015_03.on2015_03_02_Codeforces_Round__295__Div__1_.B___Cubes;


import net.egork.collections.map.EHashMap;
import net.egork.generated.collections.pair.IntIntPair;
import net.egork.io.IOUtils;
import net.egork.utils.io.InputReader;
import net.egork.utils.io.OutputWriter;

import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

public class TaskB {
	private static final long MOD = (long) (1e9 + 9);
	private Map<IntIntPair, Integer> id;
	IntIntPair[] cubes;
	IntIntPair[][] bottom;
	IntIntPair[][] top;

	public void solve(int testNumber, InputReader in, OutputWriter out) {
		int count = in.readInt();
		int[] x = new int[count];
		int[] y = new int[count];
		IOUtils.readIntArrays(in, x, y);
		id = new EHashMap<>();
		cubes = new IntIntPair[count];
		bottom = new IntIntPair[count][3];
		top = new IntIntPair[count][3];
		for (int i = 0; i < count; i++) {
			cubes[i] = new IntIntPair(x[i], y[i]);
			id.put(cubes[i], i);
		}
		for (int i = 0; i < count; i++) {
			for (int j = 0; j < 3; j++) {
				IntIntPair toTop = new IntIntPair(x[i] + j - 1, y[i] + 1);
				if (id.containsKey(toTop)) {
					top[i][j] = toTop;
				}
				IntIntPair toBottom = new IntIntPair(x[i] + j - 1, y[i] - 1);
				if (id.containsKey(toBottom)) {
					bottom[i][j] = toBottom;
				}
			}
		}
		NavigableSet<Integer> set = new TreeSet<>();
		for (int i = 0; i < count; i++) {
			if (isRemovable(cubes[i])) {
				set.add(i);
			}
		}
		long answer = 0;
		for (int i = 0; i < count; i++) {
			int toRemove;
			if ((i & 1) == 0) {
				toRemove = set.pollLast();
			} else {
				toRemove = set.pollFirst();
			}
			answer *= count;
			answer += toRemove;
			answer %= MOD;
			id.remove(cubes[toRemove]);
			for (IntIntPair candidate : bottom[toRemove]) {
				if (!id.containsKey(candidate)) {
					continue;
				}
				if (isRemovable(candidate)) {
					set.add(id.get(candidate));
				}
			}
			for (IntIntPair candidate : top[toRemove]) {
				if (!id.containsKey(candidate)) {
					continue;
				}
				for (IntIntPair on : bottom[id.get(candidate)]) {
					if (id.containsKey(on) && !isRemovable(on)) {
						set.remove(id.get(on));
					}
				}
			}
		}
		out.printLine(answer);
    }

	boolean isRemovable(IntIntPair cube) {
		for (IntIntPair toTop : top[id.get(cube)]) {
			if (!id.containsKey(toTop)) {
				continue;
			}
			boolean found = false;
			for (IntIntPair toBottom : bottom[id.get(toTop)]) {
				if (id.containsKey(toBottom) && !toBottom.equals(cube)) {
					found = true;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}
}
