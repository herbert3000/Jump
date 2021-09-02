package litecom.inca;

class QSortAlgorithm {

    void QuickSort(Primitive aprimitive[], int i, int j) {
        int k = i;
        int l = j;
        if (j > i) {
            int i1 = (int)aprimitive[(i + j) / 2].dist;
            while (k <= l)  {
                while (k < j && (int)aprimitive[k].dist > i1) 
                    k++;
                for (; l > i && (int)aprimitive[l].dist < i1; l--);
                if (k <= l) {
                    swap(aprimitive, k, l);
                    k++;
                    l--;
                }
            }
            if (i < l)
                QuickSort(aprimitive, i, l);
            if (k < j)
                QuickSort(aprimitive, k, j);
        }
    }

    private void swap(Primitive aprimitive[], int i, int j) {
        Primitive primitive = aprimitive[i];
        aprimitive[i] = aprimitive[j];
        aprimitive[j] = primitive;
    }

    public void sort(Primitive aprimitive[]) {
        QuickSort(aprimitive, 0, aprimitive.length - 1);
    }
}
