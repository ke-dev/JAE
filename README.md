
方法一：

# 防止Tensorflow运行GPU内存不足造成错误
from tensorflow.compat.v1 import ConfigProto
from tensorflow.compat.v1 import InteractiveSession
os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"
os.environ['CUDA_VISIBLE_DEVICES'] = "0, 1" #选择哪一块gpu
config = ConfigProto()
config.allow_soft_placement = True #如果你指定的设备不存在，允许TF自动分配设备
config.gpu_options.per_process_gpu_memory_fraction = 0.8  #分配百分之八十的显存给程序使用，避免内存溢出，可以自己调整
config.gpu_options.allow_growth = True   #按需分配显存，这个比较重要
session = InteractiveSession(config=config)


方法二：

os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"
os.environ['CUDA_VISIBLE_DEVICES'] = "-1"  #选择哪一块gpu--->值为-1就是说不用GPU了,直接使用CPU计算
