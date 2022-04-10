#!/usr/bin/env python
# -*- coding: utf-8 -*-

from ultra_algorithm import backupScore
from glob2 import glob
import os

f = backupScore()
for resume in glob('sample/resumes/*.xml'):
    for job_desc in glob('sample/jds/tmpvyVDgj.xml'):
        test_new = f.outputScore(resume, job_desc, 'sovren')
        print os.path.basename(glob(resume)[0]), os.path.basename(glob(job_desc)[0]),',', test_new
