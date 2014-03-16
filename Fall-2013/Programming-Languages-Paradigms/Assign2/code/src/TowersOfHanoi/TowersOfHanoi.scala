import scala.math
import scala.annotation.tailrec

def findBuffer(source :Int, dest : Int) = 3 - source - dest
  
def	moveOneDisk(source : Int, dest : Int, diskSeries : Map[Int, List[List[Int]]]) = {		
	val buffer = findBuffer(source, dest)
	  
	val sourceRods = diskSeries(source).last
	val bufferRods = diskSeries(buffer).last
	val destRods = diskSeries(dest).last
	  
	val newSourceRods = sourceRods.tail
	val newDestRods = sourceRods.head :: destRods
	  
	  
	Map(
		source -> (diskSeries(source) ::: List(newSourceRods)),
		buffer -> (diskSeries(buffer) ::: List(bufferRods)),
		dest -> (diskSeries(dest) ::: List(newDestRods)))
}

def printDiskSeries(diskSeries : Map[Int, List[List[Int]]]) {
  for(i <- 0 to diskSeries(0).size - 1) {
    for(rod <- 0 to 2) {
			print(if(diskSeries(rod)(i).isEmpty) "." else diskSeries(rod)(i).mkString.reverse)
			print(" ")      
    }
    println()
  }
}	

def towerOfHanoiIterative(n : Int, source : Int, dest : Int, diskSeries : Map[Int, List[List[Int]]]) : Map[Int, List[List[Int]]] = {
	
	val noOfMoves = (math.pow(2, n) - 1).toInt
	(1 to noOfMoves).foldLeft(diskSeries) { (diskSeries, moves) => 
			moveOneDisk((moves&moves - 1) % 3, ((moves|moves - 1) + 1) % 3, diskSeries)
		}
}

def towerOfHanoiRecursive(n : Int, source : Int, dest : Int, diskSeries : Map[Int, List[List[Int]]]) : Map[Int, List[List[Int]]] = {
	
	if(n == 1) 
	  moveOneDisk(source, dest, diskSeries)
	else {
	  val buffer = findBuffer(source, dest)
	  
	  val diskSeriesPart1 = towerOfHanoiRecursive(n - 1, source, buffer, diskSeries)
	  val diskSeriesPart2 = towerOfHanoiRecursive(1, source, dest, diskSeriesPart1)
	  val diskSeriesPart3 = towerOfHanoiRecursive(n - 1, buffer, dest, diskSeriesPart2)
	  diskSeriesPart3
	}	
}

def towerOfHanoiTailrecursive(n : Int, diskSeries : Map[Int, List[List[Int]]]) : Map[Int, List[List[Int]]] = {
	@tailrec
	def towerOfHanoiTailrecursiveHelper(n : Int, noOfMoves : Int, diskSeries : Map[Int, List[List[Int]]]) : Map[Int, List[List[Int]]] = {
		if(noOfMoves == math.pow(2, n).toInt)
		{
			diskSeries
		}
		else
		{
			val diskSeriesNew = moveOneDisk((noOfMoves&noOfMoves - 1) % 3, ((noOfMoves|noOfMoves - 1) + 1) % 3, diskSeries)
			towerOfHanoiTailrecursiveHelper(n, noOfMoves + 1, diskSeriesNew)
		}
	}
	towerOfHanoiTailrecursiveHelper(n, 1, diskSeries)
}	

val diskSeries = Map(0 -> List((1 to 5).toList), 1 -> List(List()), 2 -> List(List()))

val outputListForIterative = towerOfHanoiIterative(diskSeries(0)(0).length, 0, 2, diskSeries)
println("Output of Iterative Tower of hanoi :")
printDiskSeries(outputListForIterative)

val outputListForRecursive = towerOfHanoiRecursive(diskSeries(0)(0).length, 0, 2, diskSeries)
println("Output of Recursive Tower of hanoi :")
printDiskSeries(outputListForRecursive)

val outputListForTailRecursive = towerOfHanoiTailrecursive(diskSeries(0)(0).length, diskSeries)
println("Output of Tail Recursive Tower of hanoi :")
printDiskSeries(outputListForTailRecursive)