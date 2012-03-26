object Quicksort{
    def main(args:Array[String]){
        var nums = Array(3,9,8,2,1,5,4,6,7)
        quicksort(nums)
        println(nums.toList)
    }

    def quicksort(nums:Array[Int]){
        quicksort(nums, 0, nums.length - 1)
    }
    
    def quicksort(nums:Array[Int], start:Int, end:Int){
        if (end - start < 1){
            // no-op
        } else if (end - start == 1){
            if (nums(end) < nums(start)){
                swap(nums, start, end)
            }
        } else {
            var pivotIndex = choosePivot(nums, start, end);
            swap(nums, start, pivotIndex)
            pivotIndex = partition(nums, start, end)
            if (pivotIndex > 0){
                quicksort(nums, start, pivotIndex - 1)
            }
            if (pivotIndex < end - 1){
                quicksort(nums, pivotIndex + 1, end)        
            }
        }
    }

    def swap(nums:Array[Int], i:Int, j:Int){
        var temp = nums(i)
        nums(i) = nums(j)
        nums(j) = temp
    }

    def choosePivot(array:Array[Int], start:Int, end:Int) = start

    def partition(nums:Array[Int], start:Int, end:Int) = {
        var pivotBoundary, partitionBoundary = start + 1
        var pivot = nums(start)
        while (partitionBoundary <= end){
            if (nums(partitionBoundary) < pivot){
                swap(nums, pivotBoundary,partitionBoundary)
                pivotBoundary += 1 
            }
            partitionBoundary += 1
        }
        swap(nums, start, pivotBoundary-1)
        pivotBoundary-1
    }
}