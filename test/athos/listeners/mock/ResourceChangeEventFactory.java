package athos.listeners.mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ResourceChangeEventFactory {

	public static IResourceChangeEvent createTestEditAction(int fileSize) throws CoreException {
		IResourceChangeEvent event = mock(IResourceChangeEvent.class);
		when(event.getType()).thenReturn(IResourceChangeEvent.POST_CHANGE);
		IResourceDelta createMockResourceDelta = createChangeTestDelta("TestFile.java", fileSize);
		when(event.getDelta()).thenReturn(createMockResourceDelta);
		return event;
	}

	public static IResourceChangeEvent createEditAction(String filename, int fileSize) throws CoreException {
		IResourceChangeEvent event = mock(IResourceChangeEvent.class);
		when(event.getType()).thenReturn(IResourceChangeEvent.POST_CHANGE);
		IResourceDelta createMockResourceDelta = createChangeTestDelta(filename, fileSize);
		when(event.getDelta()).thenReturn(createMockResourceDelta);
		return event;
	}
	
	public static IResourceDelta createChangeTestDelta(String filename, long fileSize) throws CoreException {
		
		final IResourceDelta delta = mock(IResourceDelta.class);
		IFile resource = createMockResource(filename, fileSize);
		
		when(delta.getKind()).thenReturn(IResourceDelta.CHANGED);
		when(delta.getFlags()).thenReturn(IResourceDelta.CONTENT);
		when(delta.getResource()).thenReturn(resource);
		
		doAnswer(new FakeVisitorAnswer(delta)).when(delta).accept(any(IResourceDeltaVisitor.class));
		
		return delta;
		
	}
	
	
	public static IResourceChangeEvent createBuildErrorEvent(String filename, String errorMessage) throws CoreException {
		
		IMarkerDelta marker = mock(IMarkerDelta.class);
		when(marker.getType()).thenReturn(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER);
		when(marker.getAttribute("severity")).thenReturn("2");
		when(marker.getAttribute("message")).thenReturn(errorMessage);
		when(marker.getKind()).thenReturn(IResourceDelta.CHANGED);

		IMarkerDelta[] markers = new IMarkerDelta[]{marker};
		
		final IResourceDelta delta = mock(IResourceDelta.class);
		
		doAnswer(new FakeVisitorAnswer(delta)).when(delta).accept(any(IResourceDeltaVisitor.class));
		
		when(delta.getFlags()).thenReturn(IResourceDelta.MARKERS);
		
		IFile aresource = createMockResource(filename, (long)33);
		when(delta.getResource()).thenReturn(aresource);
		
		when(delta.getMarkerDeltas()).thenReturn(markers);
		
		IResourceChangeEvent event = mock(IResourceChangeEvent.class);
		when(event.getType()).thenReturn(IResourceChangeEvent.POST_CHANGE);
		when(event.getDelta()).thenReturn(delta);
		return event;
	}
	
	public static IFile createMockResource(String filename, long fileSIze) {
		
		String[] split = filename.split("\\.");
		
		IFile resource = mock(IFile.class);
		
		IPath path = mock(IPath.class);
		when(path.toString()).thenReturn(filename);
		
		File file = mock(File.class);
		when(file.getName()).thenReturn(filename);
		when(file.getPath()).thenReturn(filename);
		when(file.length()).thenReturn(fileSIze);
		
		when(path.toFile()).thenReturn(file);
		
		if (split.length>1)
			when(path.getFileExtension()).thenReturn(split[1]);

		when(resource.getLocation()).thenReturn(path);

		// separates "filename" of ".java"
		when(resource.getName()).thenReturn(split[0]);
		
		if (split.length>1)
			when(resource.getFileExtension()).thenReturn(split[1]);
		
		return resource;
	}

}
