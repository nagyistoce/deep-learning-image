package home.mutant.opelcl;

import static org.jocl.CL.*;

import java.util.ArrayList;
import java.util.List;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;


public class OpenClWrapper 
{
	cl_command_queue commandQueue;
	cl_kernel kernel;
	cl_program program;
	cl_context context;
	List<cl_mem> memObjects = new ArrayList<cl_mem>();
	
	public OpenClWrapper(String source, String functionName)
	{
        final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_GPU;
        final int deviceIndex = 0;

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
        
        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];
        
        // Obtain a device ID 
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        context = clCreateContext(contextProperties, 1, new cl_device_id[]{device}, null, null, null);
        commandQueue = clCreateCommandQueue(context, device, 0, null);
        program = clCreateProgramWithSource(context, 1, new String[]{ source }, null, null);
        clBuildProgram(program, 0, null, null, null, null);
        kernel = clCreateKernel(program, functionName, null);
	}
	
	public cl_mem addInputMemObject(float[] src)
	{
		cl_mem clCreateBuffer = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * src.length, Pointer.to(src), null);
		memObjects.add(clCreateBuffer);
        clSetKernelArg(kernel, memObjects.size()-1, Sizeof.cl_mem, Pointer.to(clCreateBuffer));
        return clCreateBuffer;
	}
	
	public cl_mem addInputOutputMemObject(float[] src)
	{
		cl_mem clCreateBuffer = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * src.length, Pointer.to(src), null);
		memObjects.add(clCreateBuffer);
        clSetKernelArg(kernel, memObjects.size()-1, Sizeof.cl_mem, Pointer.to(clCreateBuffer));
        return clCreateBuffer;
	}
	
	public cl_mem addInputMemObject(long length)
	{
		cl_mem clCreateBuffer = clCreateBuffer(context, CL_MEM_READ_ONLY , Sizeof.cl_float * length, null, null);
		memObjects.add(clCreateBuffer);
        clSetKernelArg(kernel, memObjects.size()-1, Sizeof.cl_mem, Pointer.to(clCreateBuffer));
        return clCreateBuffer;
	}	
	public cl_mem addOutputMemObject(long length)
	{
		cl_mem clCreateBuffer = clCreateBuffer(context, CL_MEM_READ_WRITE, Sizeof.cl_float * length, null, null);
		memObjects.add(clCreateBuffer);
		clSetKernelArg(kernel, memObjects.size()-1, Sizeof.cl_mem, Pointer.to(clCreateBuffer));
		return clCreateBuffer;
	}
	public void copyDataDtoH(cl_mem deviceObject, float[] dst)
	{
        clEnqueueReadBuffer(commandQueue, deviceObject, CL_TRUE, 0,  dst.length * Sizeof.cl_float, Pointer.to(dst), 0, null, null);
	}
	public int copyDataHtoD(cl_mem deviceObject, float[] src)
	{
        return clEnqueueWriteBuffer(commandQueue, deviceObject, CL_TRUE, 0,  src.length * Sizeof.cl_float, Pointer.to(src), 0, null, null);
	}
	public int runKernel(long globalworkSize, long localWorksize)
	{
        return clEnqueueNDRangeKernel(commandQueue, kernel, 1, null, new long[]{globalworkSize}, new long[]{localWorksize}, 0, null, null);
	}
	public static float[] bytesToFloats(byte[] bytes)
	{
		float[] floats = new float[bytes.length];
		for (int i = 0; i < bytes.length; i++) 
		{
			int pixel = bytes[i];
			if (pixel<0)pixel+=255;
			floats[i]=pixel;
		}
		return floats;
	}
	public static float[] listBytesToFloats(List<byte[]> subImages) 
	{
		float[] floats = new float[subImages.size()*subImages.get(0).length];
		int index=0;
		for (byte[] bytes : subImages) 
		{
			for (int i = 0; i < bytes.length; i++) 
			{
				int pixel = bytes[i];
				if (pixel<0)pixel+=255;
				floats[index++]=pixel;
			}
		}
		return floats;
	}
	public void release()
	{
		for(cl_mem mem:memObjects)
		{
			clReleaseMemObject(mem);
		}
		memObjects.clear();
        clReleaseKernel(kernel);
        clReleaseProgram(program);
        clReleaseCommandQueue(commandQueue);
        clReleaseContext(context);
	}
	public int finish()
	{
		return clFinish(commandQueue);
	}
}
