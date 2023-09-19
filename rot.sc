

GRot {

	// Gruppenrotation

	var rotations, sizeLcm, seqssize;

	getrotations {
		^rotations
	}

	*new {
		arg idxarrs, items=nil, appendhead=false, excludehead=false;
		// Wenn es items gibt, die items werden ausgegeben, statt die idxarrs selbst
		^super.new.prInit(idxarrs, items, appendhead, excludehead)
	}

	prLcmOfNums {
		arg nums;
		var tmpLcm = nums[0].lcm(nums[1]);
		var idx = 2;
		while {idx < nums.size} {
			tmpLcm = tmpLcm.lcm(nums[idx]);
			idx = idx + 1;
		};
		^tmpLcm
	}

	prInit {
		arg idxarrs, items, appendhead, excludehead;
		seqssize = idxarrs.size;
		sizeLcm = this.prLcmOfNums(idxarrs.collect {arg a; a.size});
		rotations = sizeLcm.collect {
			arg i;
			idxarrs.collect {
				arg idx_subarr;
				if (items != nil) {
					// dann sammle die items
					idx_subarr.rotate(i * -1).collect {
						arg arr;
						items.at(arr)
					}
				} {
					// sonst sammle einfach die indices selbst
					idx_subarr.rotate(i * -1)
				}
			}
		};
		if (appendhead) {
			rotations = rotations.insert(sizeLcm, rotations.at(0))
		};
		if (excludehead) {
			rotations = rotations[1..]
		}
	}

	fltn {
		^rotations.collect {
			arg rot;
			rot.collect {
				arg sublist;
				sublist.flatten
			}
		}
	}

	countzip {
		var dict = Dictionary.new;
		this.zip.do {
			arg x;
			dict.put(x, dict.atFail(x, 0) + 1)
		};
		^dict
	}

	post {
		arg flat=true;
		if (flat) {
			this.fltn.do {arg x,i; "%. %\n".postf(i, x)}
		} {
			this.getrotations.do {arg x,i; "%. %\n".postf(i, x)}
		};
		^nil
	}

	zip {
		^this.fltn.collect {
			arg lst;
			lst[0].size.collect {
				arg i;
				lst.collect {arg l; l[i..]}.lace(lst.size)
			}
		}.flatten
	}

	clmns {
		^seqssize.collect {
			arg i;
			this.fltn.collect {
				arg rot;
				rot[i]
			}.flatten
		}
	}
}

